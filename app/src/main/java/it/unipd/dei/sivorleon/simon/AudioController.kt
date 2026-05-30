package it.unipd.dei.sivorleon.simon

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.math.roundToInt
import kotlin.math.sin

class AudioController {
    // Audio settings
    private val sampleRate = 44100
    private val durationInSeconds : Double = (controller.animationDuration.toDouble()/1000)
    private val numSamples : Int = (sampleRate * durationInSeconds).roundToInt()


    //AudioTrackSingleton
    private var audioTracks : List<AudioTrack>

    init {
        val audioAttributes = AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_MEDIA)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build()

        val audioFormat = AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(sampleRate)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()

        // Since it's PCM_16BIT, each sample is 2 bytes (Short)
        val totalSizeInBytes = numSamples * 2

        //Create sounds to save in each AudioTrack object
        val fadeDuration = (sampleRate * 0.05).toInt()
        val indexStartFade = numSamples - fadeDuration //Fading to begin 10ms before end of sine wave
        var waves : List<ShortArray> = buildList {
            for (tile in tiles) {
                val samples = ShortArray(numSamples)

                //Generate the Sine Wave PCM data
                for (i in 0 until numSamples) {
                    val time = i.toDouble() / sampleRate
                    val angle = 2.0 * Math.PI * tile.tone * time

                    var amplitude : Double = if (i > indexStartFade) {
                        Short.MAX_VALUE * (numSamples - i).toDouble() / fadeDuration //linear Fade
                    } else {
                        Short.MAX_VALUE.toDouble() //max amplitude
                    }
                    samples[i] = (sin(angle) * amplitude).toInt().toShort()
                }

                add(samples)
            }
        }

        audioTracks = buildList {
            for (wave in waves) {
                val track = AudioTrack.Builder()
                    .setAudioAttributes(audioAttributes)
                    .setAudioFormat(audioFormat)
                    .setBufferSizeInBytes(totalSizeInBytes) // Pass the total size here
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                track.write(wave, 0, numSamples)
                add(track)
            }
        }
    }

    fun playTile(index: Int) {
        audioTracks[index].stop()
        audioTracks[index].setPlaybackHeadPosition(0)
        audioTracks[index].play()
    }

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AudioController? = null

        fun getController(): AudioController {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = AudioController()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}