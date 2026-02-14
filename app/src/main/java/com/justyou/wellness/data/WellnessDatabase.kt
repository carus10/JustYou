package com.justyou.wellness.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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

@Database(
    entities = [
        DailyTracking::class,
        UserGoals::class,
        Affirmation::class,
        NegativeThought::class,
        ChallengeStep::class
    ],
    version = 2,
    exportSchema = false
)
abstract class WellnessDatabase : RoomDatabase() {
    abstract fun dailyTrackingDao(): DailyTrackingDao
    abstract fun userGoalsDao(): UserGoalsDao
    abstract fun affirmationDao(): AffirmationDao
    abstract fun negativeThoughtDao(): NegativeThoughtDao
    abstract fun challengeStepDao(): ChallengeStepDao

    companion object {
        @Volatile
        private var INSTANCE: WellnessDatabase? = null

        fun getDatabase(context: Context): WellnessDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WellnessDatabase::class.java,
                    "wellness_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
