package com.justyou.wellness.util

/**
 * Su seviyesi yüzdesini hesaplar.
 * @return 0.0f ile 1.0f arası clamp edilmiş değer
 */
fun calculateWaterLevel(current: Int, goal: Int): Float {
    if (goal <= 0) return 0f
    return (current.toFloat() / goal).coerceIn(0f, 1f)
}

/**
 * Kalori ekmek aşamasını hesaplar.
 * @return 0-4 arası: 0=yok, 1=çeyrek, 2=yarım, 3=%75, 4=tam
 */
fun calculateBreadStage(current: Int, goal: Int): Int {
    if (goal <= 0) return 0
    if (current <= 0) return 0
    return minOf(4, (current * 4) / goal)
}

/**
 * Adım figür aşamasını hesaplar.
 * @return 0-3 arası: 0=en kilolu(sol), 3=en ince(sağ)
 */
fun calculateStepStage(current: Int, goal: Int): Int {
    if (goal <= 0) return 0
    if (current <= 0) return 0
    return minOf(3, (current * 3) / goal)
}
