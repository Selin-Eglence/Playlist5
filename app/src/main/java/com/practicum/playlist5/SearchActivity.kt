package com.practicum.playlist5

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SearchActivity : AppCompatActivity() {

    private val ItunesBaseUrl = "https://itunes.apple.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(ItunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    private val trackService = retrofit.create(TrackAPI::class.java)
    val tracks = mutableListOf<Track>()
    private val adapter= TrackAdapter(tracks)

    private lateinit var trackset: RecyclerView
    private lateinit var arrow :Button
    private lateinit var inputEditText: EditText
    private lateinit var clearButton:ImageView
    private lateinit var errorMessage:LinearLayout
    private lateinit var errorImage:ImageView
    private lateinit var errorText:TextView
    private lateinit var refreshButton:Button





    private var text: String = ""
    private fun hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_poisk)



        trackset = findViewById(R.id.recyclerview)
        trackset.layoutManager = LinearLayoutManager(this)
        trackset.adapter = adapter
        arrow = findViewById(R.id.light_mode)
        inputEditText = findViewById(R.id.inputEditText)
        clearButton = findViewById(R.id.clearIcon)
        errorMessage = findViewById(R.id.ErrorMessage)
        errorImage = findViewById(R.id.ErrorImage)
        errorText = findViewById(R.id.ErrorText)
        refreshButton = findViewById(R.id.RefreshButton)
        inputEditText.requestFocus()



        clearButton.setOnClickListener {
            inputEditText.text.clear()
            tracks.clear()
            clearButton.visibility = View.GONE
            adapter.notifyDataSetChanged()
            hideKeyboard(inputEditText)
        }
        arrow.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        fun clearButtonVisibility(s: CharSequence?): Int {
            return if (s.isNullOrEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        refreshButton.setOnClickListener {
            search() }


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

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                search()
            }
            false}
        inputEditText.addTextChangedListener(simpleTextWatcher)
    }


    private fun search() {
    trackService.search(inputEditText.text.toString())
    .enqueue(object : Callback<TrackResponse> {
        override fun onResponse(
            call: Call<TrackResponse>,
            response: Response<TrackResponse>
        ) {
            if (response.code() == 200) {
                    tracks.clear()
                    if (response.body()?.results?.isNotEmpty() == true) {
                        tracks.clear()
                        errorMessage.isVisible = false
                        tracks.addAll(response.body()?.results!!)
                        adapter.notifyDataSetChanged()

                    }
                    else {
                        tracks.clear()
                        adapter.notifyDataSetChanged()
                        errorMessage.isVisible = true
                        refreshButton.isVisible = false
                        errorText.text = getString(R.string.nothing_found)
                        errorImage.setImageResource(R.drawable.emodji_error)
                    }
                }

                else {
                tracks.clear()
                adapter.notifyDataSetChanged()
                    errorText.text = getString(R.string.something_wrong)
                    errorImage.setImageResource(R.drawable.noconnection_error)
                    refreshButton.isVisible = true

                }
            }

        override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
            errorText.text = getString(R.string.something_wrong)
            errorImage.setImageResource(R.drawable.noconnection_error)
            refreshButton.isVisible = true
            tracks.clear()
            adapter.notifyDataSetChanged()
        }
    })
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(INPUT, text)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        text = savedInstanceState.getString(INPUT, text)
        inputEditText.setText(text)
    }

    companion object {
        const val INPUT = "INPUT"
    }

    }


