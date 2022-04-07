package ru.kotofeya.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import ru.kotofeya.databinding.ActivityMainBinding
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import ru.kotofeya.R
import ru.kotofeya.database.ListItemEntity
import ru.kotofeya.viewModel.DataModel

import java.lang.StringBuilder

class MainActivity : AppCompatActivity(), RecognitionListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dataModel: DataModel
    private val RECORD_AUDIO_REQUEST_CODE = 1
    private val TAG = this.javaClass.simpleName
    private var hasRecordPermission = false

    private lateinit var speechRecognizer: SpeechRecognizer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dataModel = ViewModelProvider((this), DataModel.Factory(this)).get(DataModel::class.java)
        dataModel.load()
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, ListFragment.newInstance())
            .commit()
        checkPermission()
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        speechRecognizer.setRecognitionListener(this)
    }



    fun startRec(){
        checkPermission()
        if(hasRecordPermission) {
            val recIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            speechRecognizer.startListening(recIntent)
        } else {
            Toast.makeText(this, resources.getString(R.string.please_set_rec_permissions),
                Toast.LENGTH_SHORT).show()
        }
    }

    fun stopRec(){
        speechRecognizer.stopListening()
    }

    private fun checkPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED){
            requestPermission()
        }
        else hasRecordPermission = true
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.RECORD_AUDIO),
            RECORD_AUDIO_REQUEST_CODE)
    }

    override fun onReadyForSpeech(p0: Bundle?) {
        Log.d(TAG, "onReadyForSpeech $p0")
    }

    override fun onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech()")
    }

    override fun onRmsChanged(p0: Float) {
//        Log.d(TAG, "onRmsChanged $p0")
    }

    override fun onBufferReceived(p0: ByteArray?) {
        Log.d(TAG, "onBufferReceived $p0")
    }

    override fun onEndOfSpeech() {
        Log.d(TAG, "onEndOfSpeech()")
    }

    override fun onError(p0: Int) {
        Log.d(TAG, "onError $p0")
    }

    override fun onResults(p0: Bundle?) {
        Log.d(TAG, "onResults $p0")
        val textList = p0!!.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val strBuilder = StringBuilder()
        for(text in textList!!){
            strBuilder.append(text)
        }
        val listItemEntity = ListItemEntity(listId = 0, value = strBuilder.toString())
        lifecycleScope.launch {
            dataModel.insertNewListItemEntity(listItemEntity)
        }
    }

    override fun onPartialResults(p0: Bundle?) {
        Log.d(TAG, "onPartialResults $p0")
    }

    override fun onEvent(p0: Int, p1: Bundle?) {
        Log.d(TAG, "onEvent $p0")
    }

    override fun onDestroy() {
        super.onDestroy()
        speechRecognizer.destroy()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == RECORD_AUDIO_REQUEST_CODE && grantResults.isNotEmpty()){
            if(grantResults[0] != PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, resources.getString(R.string.speech_rec_unavailable),
                    Toast.LENGTH_SHORT).show()
            } else {
                hasRecordPermission = true
            }
        }
    }
}



//пилон 07.03, 09.03, 11.03, 16.03, 23.03, 25.03