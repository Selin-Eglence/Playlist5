package com.practicum.playlist5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.EditText

class SearchActivity : AppCompatActivity() {

    private var text: String = ""
    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poisk)

        val arrow = findViewById<Button>(R.id.light_mode)
        arrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        val clearButton = findViewById<ImageView>(R.id.clearIcon)
        inputEditText.requestFocus()



        clearButton.setOnClickListener {
            inputEditText.setText("")
            clearButton.visibility=View.GONE
            hideKeyboard(inputEditText)
        }



        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // empty
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               text = inputEditText.text.toString()
                clearButton.visibility = clearButtonVisibility(s)

            }

            override fun afterTextChanged(s: Editable?) {
                // empty
            }
        }
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }



    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT, text)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val inputEditText = findViewById<EditText>(R.id.inputEditText)
        text = savedInstanceState.getString(INPUT, text)
        inputEditText.setText(text)
    }

    companion object {
        const val INPUT = "INPUT"
    }
    }
