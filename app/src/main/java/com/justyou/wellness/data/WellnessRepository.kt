package com.justyou.wellness.data

import com.justyou.wellness.data.dao.AffirmationDao
import com.justyou.wellness.data.dao.ChallengeStepDao
import com.justyou.wellness.data.dao.DailyTrackingDao
import com.justyou.wellness.data.dao.NegativeThoughtDao
import com.justyou.wellness.data.dao.UserGoalsDao
import com.justyou.wellness.data.entity.Affirmation
import com.justyou.wellness.data.entity.ChallengeStep
import com.justyou.wellness.data.entity.DailyTracking
import com.justyou.wellness.data.entity.NegativeThought
import com.justyou.wellness.data.entity.UserGoals
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class WellnessRepository(
    private val dailyTrackingDao: DailyTrackingDao,
    private val userGoalsDao: UserGoalsDao,
    private val affirmationDao: AffirmationDao,
    private val negativeThoughtDao: NegativeThoughtDao,
    private val challengeStepDao: ChallengeStepDao
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private fun today(): String = LocalDate.now().format(dateFormatter)

    // Günlük takip
    fun getTodayTracking(): Flow<DailyTracking?> = dailyTrackingDao.getByDate(today())

    suspend fun addWater(amount: Int) {
        val date = today()
        val current = dailyTrackingDao.getByDateOnce(date)
        val tracking = current?.copy(waterIntake = current.waterIntake + amount)
            ?: DailyTracking(date = date, waterIntake = amount)
        dailyTrackingDao.upsert(tracking)
    }

    suspend fun addCalories(amount: Int) {
        val date = today()
        val current = dailyTrackingDao.getByDateOnce(date)
        val tracking = current?.copy(calorieIntake = current.calorieIntake + amount)
            ?: DailyTracking(date = date, calorieIntake = amount)
        dailyTrackingDao.upsert(tracking)
    }

    suspend fun addSteps(amount: Int) {
        val date = today()
        val current = dailyTrackingDao.getByDateOnce(date)
        val tracking = current?.copy(stepCount = current.stepCount + amount)
            ?: DailyTracking(date = date, stepCount = amount)
        dailyTrackingDao.upsert(tracking)
    }

    // Hedefler
    fun getGoals(): Flow<UserGoals?> = userGoalsDao.getGoals()

    suspend fun updateGoals(goals: UserGoals) = userGoalsDao.upsert(goals)

    // Telkinler
    fun getAllAffirmations(): Flow<List<Affirmation>> = affirmationDao.getAll()

    suspend fun addAffirmation(text: String) {
        affirmationDao.insert(Affirmation(text = text))
    }

    suspend fun deleteAffirmation(affirmation: Affirmation) = affirmationDao.delete(affirmation)

    // OOD
    fun getAllNegativeThoughts(): Flow<List<NegativeThought>> = negativeThoughtDao.getAll()

    suspend fun addNegativeThought(text: String, response: String) {
        negativeThoughtDao.insert(NegativeThought(text = text, response = response))
    }

    suspend fun deleteNegativeThought(thought: NegativeThought) = negativeThoughtDao.delete(thought)

    // Challenge
    fun getChallengeSteps(): Flow<List<ChallengeStep>> = challengeStepDao.getAll()

    suspend fun updateChallengeStep(step: ChallengeStep) = challengeStepDao.upsert(step)

    // İstatistik
    fun getWeeklyTracking(): Flow<List<DailyTracking>> {
        val endDate = today()
        val startDate = LocalDate.now().minusDays(6).format(dateFormatter)
        return dailyTrackingDao.getRange(startDate, endDate)
    }

    fun getTrackingForDays(days: Int): Flow<List<DailyTracking>> {
        val endDate = today()
        val startDate = LocalDate.now().minusDays(days.toLong() - 1).format(dateFormatter)
        return dailyTrackingDao.getRange(startDate, endDate)
    }

    fun getTrackingRange(startDate: String, endDate: String): Flow<List<DailyTracking>> {
        return dailyTrackingDao.getRange(startDate, endDate)
    }

    // Rastgele günlük telkin
    suspend fun getRandomAffirmationText(): String? {
        return affirmationDao.getAllOnce().randomOrNull()?.text
    }

    // Rastgele "Yeni Sen" çifti
    suspend fun getRandomNegativeThought(): NegativeThought? {
        return negativeThoughtDao.getAllOnce().randomOrNull()
    }
}
