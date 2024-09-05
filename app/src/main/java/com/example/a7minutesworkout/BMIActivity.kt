package com.example.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.a7minutesworkout.databinding.ActivityBmiBinding
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {

    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC UNIT VIEW"
        private const val US_UNITS_VIEW = "US UNIT VIEW" // Us Unit view

    }

    private var currentVisibleView: String = METRIC_UNITS_VIEW

    private var binding: ActivityBmiBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBmiBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarBmiActivity)
        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            supportActionBar?.title = "CALCULATE BMI"
        }
        binding?.toolbarBmiActivity?.setNavigationOnClickListener {
            onBackPressed()

        }

        makeVisibleMetricUnitsView()

        // We used this bcs it will give us Id
        binding?.rgUnits?.setOnCheckedChangeListener { _, checkedId: Int ->
            if (checkedId == R.id.rbMetricUnits) {
                makeVisibleMetricUnitsView()
            } else {
                makeVisibleUSUnitsView()
            }
        }

        binding?.btnCalculateUnits?.setOnClickListener {
           calculateUnits()
        }
    }

    private fun makeVisibleMetricUnitsView() {
        currentVisibleView = METRIC_UNITS_VIEW
        binding?.tilMetricUnitHeight?.visibility = View.VISIBLE
        binding?.tilMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilUsMetricUnitWeight?.visibility = View.INVISIBLE
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.INVISIBLE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.INVISIBLE

        binding?.etMetricUnitHeight?.text!!.clear() // height value is cleared
        binding?.etMetricUnitWeight?.text!!.clear() // weight value is cleared

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE
    }

    private fun makeVisibleUSUnitsView() {
        currentVisibleView = US_UNITS_VIEW // current view is updating here
        binding?.tilMetricUnitHeight?.visibility = View.INVISIBLE
        binding?.tilMetricUnitWeight?.visibility = View.INVISIBLE

        binding?.tilUsMetricUnitWeight?.visibility = View.VISIBLE
        binding?.tilMetricUsUnitHeightFeet?.visibility = View.VISIBLE
        binding?.tilMetricUsUnitHeightInch?.visibility = View.VISIBLE

        binding?.etUsMetricUnitWeight?.text!!.clear()
        binding?.etUsMetricUnitHeightFeet?.text!!.clear()
        binding?.etUsMetricUnitHeightInch?.text!!.clear()

        binding?.llDisplayBMIResult?.visibility = View.INVISIBLE


    }

    private fun displayBMIResult(bmi: Float) {

        val bmiLabel: String
        val bmiDescription: String

        if (bmi.compareTo(15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops!, You really need to take care of yourself! Eat healthy"
        } else if (bmi.compareTo(15f) > 0 && bmi.compareTo(16f) <= 0) {
            bmiLabel = " severely underweight"
            bmiDescription = "Oops!, You really need to take care of yourself! Eat healthy"
        } else if (bmi.compareTo(16f) > 0 && bmi.compareTo(18.5f) <= 0) {
            bmiLabel = "  underweight"
            bmiDescription = "Oops!, You really need to take care of yourself! Eat healthy"
        } else if (bmi.compareTo(18.5f) > 0 && bmi.compareTo(25f) <= 0) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulation! You are in good shape"
        } else if (bmi.compareTo(25f) > 0 && bmi.compareTo(30f) <= 0) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops!, You really need to take care of yourself! Workout"
        } else if (bmi.compareTo(30f) > 0 && bmi.compareTo(35f) <= 0) {
            bmiLabel = "Obese class | (Moderately obese)"
            bmiDescription = "Oops!, You are in very dangerous condition! Act now!"
        } else if (bmi.compareTo(35f) > 0 && bmi.compareTo(40f) <= 0) {
            bmiLabel = "Obese class | (Severally obese)"
            bmiDescription = "Oops!, You are in very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese class | (Very Severally obese)"
            bmiDescription = "Oops!, You are in very dangerous condition! Act now!"
        }

        val bmiValue = BigDecimal(
            bmi.toDouble()
        )
            .setScale(2, RoundingMode.HALF_EVEN).toString()

        binding?.llDisplayBMIResult?.visibility = View.VISIBLE
        binding?.tvBMIValue?.text = bmiValue
        binding?.tvBMIType?.text = bmiLabel
        binding?.tvBMIDescription?.text = bmiDescription

    }

    // Here checking that user is adding value of height or weight is yes or not
    private fun validateMetricUnits(): Boolean {
        var isValid = true
        if (binding?.etMetricUnitWeight?.text.toString().isEmpty()) {
            isValid = false
        } else if (binding?.etMetricUnitHeight?.text.toString().isEmpty()) {
            isValid = false
        }
        return isValid
    }

    private fun calculateUnits() {

        if (currentVisibleView == METRIC_UNITS_VIEW) {
            if (validateMetricUnits()) {
                // Here calculating the height in cm
                val heightValue: Float =
                    binding?.etMetricUnitHeight?.text.toString().toFloat() / 100

                // here calculating the weight in kg
                val weightValue: Float = binding?.etMetricUnitWeight?.text.toString().toFloat()
                // Formula for the BMI calculation
                val bmi = weightValue / (heightValue * heightValue)
                displayBMIResult(bmi)

            } else {
                Toast.makeText(
                    this,
                    "Please enter some value",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            if (validateUsUnits()) {
                val usUnitHeightValueFeet : String =
                    binding?.etUsMetricUnitHeightFeet?.text.toString()

                val usUnitHeightValueInch : String =
                    binding?.etUsMetricUnitHeightInch?.text.toString()

                val usUnitWeightValue : Float = binding?.etUsMetricUnitWeight?.text.toString().toFloat()

                // here the height feet and inch values are merged and multiplied by 12 for conversion
                val heightValue =
                    usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat() * 12

                val bmi = 703 * (usUnitWeightValue / (heightValue * heightValue))
                displayBMIResult(bmi)


            } else {
                Toast.makeText(this,"Please enter value",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun validateUsUnits(): Boolean {
        var isValid = true

        when {
            binding?.etUsMetricUnitWeight?.text.toString().isEmpty() -> {
                isValid = false
            }

            binding?.etUsMetricUnitHeightFeet?.text.toString().isEmpty() -> {
                isValid = false
            }

            binding?.etUsMetricUnitHeightInch?.text.toString().isEmpty() -> {
                isValid = false
            }
        }
        return isValid
    }
}

