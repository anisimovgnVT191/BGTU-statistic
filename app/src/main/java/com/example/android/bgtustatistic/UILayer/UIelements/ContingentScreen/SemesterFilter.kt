package com.example.android.bgtustatistic.UILayer.UIelements.ContingentScreen

enum class SemesterFilter{
    all, firstSemester, secondSemester;

    companion object{
        fun semesterFromMonthNumber(month: Int) =
            when(month){
                in 9..12 -> firstSemester
                in 2..6 -> secondSemester
                1 -> firstSemester
                else -> all
            }
    }
}