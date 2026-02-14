package com.justyou.wellness.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justyou.wellness.data.WellnessRepository
import com.justyou.wellness.data.entity.DailyTracking
import com.justyou.wellness.data.entity.UserGoals
import com.justyou.wellness.util.MetricType
import com.justyou.wellness.util.TrendIndicator
import com.justyou.wellness.util.determineTrendIndicator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

enum class TimeRange(val label: String, val days: Int) {
    DAY_1("1 Gün", 1),
    DAYS_30("30 Gün", 30),
    MONTHS_6("6 Ay", 180),
    YEAR_1("1 Yıl", 365)
}

enum class CalendarMetric(val label: String) {
    KALORI("Kalori"),
    SU("Su"),
    ADIM("Adım")
}

data class PeriodStats(
    val totalWater: Int,
    val totalCalorie: Int,
    val totalStep: Int,
    val avgWater: Int,
    val avgCalorie: Int,
    val avgStep: Int
)

data class IstatistikState(
    val stats: PeriodStats = PeriodStats(0, 0, 0, 0, 0, 0),
    val waterTrend: TrendIndicator = TrendIndicator.RED_DOWN,
    val calorieTrend: TrendIndicator = TrendIndicator.GREEN_DOWN,
    val stepTrend: TrendIndicator = TrendIndicator.RED_DOWN,
    val selectedRange: TimeRange = TimeRange.DAYS_30,
    val trackingData: List<DailyTracking> = emptyList(),
    val periodWaterGoal: Int = 0,
    val periodCalorieGoal: Int = 0,
    val periodStepGoal: Int = 0,
    val calendarMetric: CalendarMetric = CalendarMetric.KALORI,
    val calendarMonth: YearMonth = YearMonth.now(),
    val calendarData: Map<Int, DailyTracking> = emptyMap(),
    val goals: UserGoals = UserGoals()
)

class IstatistikViewModel(private val repository: WellnessRepository) : ViewModel() {

    private val _selectedRange = MutableStateFlow(TimeRange.DAY_1)
    private val _calendarMetric = MutableStateFlow(CalendarMetric.KALORI)
    private val _calendarMonth = MutableStateFlow(YearMonth.now())

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @OptIn(ExperimentalCoroutinesApi::class)
    private val calendarDataFlow: Flow<Map<Int, DailyTracking>> = _calendarMonth.flatMapLatest { month ->
        val startDate = month.atDay(1).format(dateFormatter)
        val endDate = month.atEndOfMonth().format(dateFormatter)
        repository.getTrackingRange(startDate, endDate).map { list ->
            list.associateBy { LocalDate.parse(it.date, dateFormatter).dayOfMonth }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val state: StateFlow<IstatistikState> = _selectedRange.flatMapLatest { range ->
        combine(
            repository.getTrackingForDays(range.days),
            repository.getGoals().map { it ?: UserGoals() },
            _calendarMetric,
            _calendarMonth,
            calendarDataFlow
        ) { data, goals, metric, month, calData ->
            val totalWater = data.sumOf { it.waterIntake }
            val totalCalorie = data.sumOf { it.calorieIntake }
            val totalStep = data.sumOf { it.stepCount }
            val days = range.days
            val stats = PeriodStats(
                totalWater = totalWater,
                totalCalorie = totalCalorie,
                totalStep = totalStep,
                avgWater = totalWater / days.coerceAtLeast(1),
                avgCalorie = totalCalorie / days.coerceAtLeast(1),
                avgStep = totalStep / days.coerceAtLeast(1)
            )
            val periodWaterGoal = goals.waterGoal * days
            val periodCalorieGoal = goals.calorieGoal * days
            val periodStepGoal = goals.stepGoal * days

            IstatistikState(
                stats = stats,
                waterTrend = determineTrendIndicator(totalWater, periodWaterGoal, MetricType.WATER),
                calorieTrend = determineTrendIndicator(totalCalorie, periodCalorieGoal, MetricType.CALORIE),
                stepTrend = determineTrendIndicator(totalStep, periodStepGoal, MetricType.STEP),
                selectedRange = range,
                trackingData = data,
                periodWaterGoal = periodWaterGoal,
                periodCalorieGoal = periodCalorieGoal,
                periodStepGoal = periodStepGoal,
                calendarMetric = metric,
                calendarMonth = month,
                calendarData = calData,
                goals = goals
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), IstatistikState())

    fun setTimeRange(range: TimeRange) {
        _selectedRange.value = range
    }

    fun setCalendarMetric(metric: CalendarMetric) {
        _calendarMetric.value = metric
    }

    fun previousMonth() {
        _calendarMonth.value = _calendarMonth.value.minusMonths(1)
    }

    fun nextMonth() {
        _calendarMonth.value = _calendarMonth.value.plusMonths(1)
    }
}
