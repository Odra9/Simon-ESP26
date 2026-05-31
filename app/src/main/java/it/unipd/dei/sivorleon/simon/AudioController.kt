package it.unipd.dei.sivorleon.simon

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlin.math.roundToInt
import kotlin.math.sin

/**
 * Singleton Controller to manage UI interactions
 * Called by GameController
 */
class AudioController {
    // Audio settings
    private val sampleRate = 44100 // A Higher number leads to more quality but also more memory usage
    private val durationInSeconds : Double = (GameController.getController().animationDuration.toDouble()/1000)
    private val numSamples : Int = (sampleRate * durationInSeconds).roundToInt()


    // For each sound used in game, a different audioTrack object is created, which holds the values of its sine wave in memory
    private var audioTracks : List<AudioTrack>

    init {
        val audioAttributes = AudioAttributes.Builder()
            /*
                Usage is Game Event

                AudioAttributes.USAGE_ASSISTANCE_SONIFICATION could also be used
                if muting sounds if phone is on silent mode is desired behaviour
             */
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)  // Type is UI
            .build()

        val audioFormat = AudioFormat.Builder()
            .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
            .setSampleRate(sampleRate)
            .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
            .build()

        // Since it's PCM_16BIT, each sample is 2 bytes (Short)
        val totalSizeInBytes = numSamples * 2

        /*
            Calculate the sine waves to be stored into the audioTrack objects
            Each sound fades out in the last 50ms to prevent 'popping' audio issues
        */
        // Fading to begin 50ms before end of sine wave
        val fadeDuration = (sampleRate * 0.05).toInt()
        // sample index at which to start the fade
        val indexStartFade = numSamples - fadeDuration
        var waves : List<ShortArray> = buildList {
            for (tile in tiles) {
                val samples = ShortArray(numSamples)

                //Generate the Sine Wave PCM data
                for (i in 0 until numSamples) {
                    val time = i.toDouble() / sampleRate
                    val angle = 2.0 * Math.PI * tile.tone * time

                    /*  Linear fade: wave amplitude decreases linearly to zero
                        Base amplitude is the max amplitude, the integer limit for Short variables
                     */
                    var amplitude : Double = if (i > indexStartFade) {
                        Short.MAX_VALUE * (numSamples - i).toDouble() / fadeDuration // linear Fade
                    } else {
                        Short.MAX_VALUE.toDouble() // max amplitude
                    }
                    samples[i] = (sin(angle) * amplitude).toInt().toShort()
                }

                add(samples)
            }
        }

        // generate audioTracks
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

    /**
        Play the relative sound for each Tile

        @param[index] Index of Tile
     */
    fun playTile(index: Int) {
        // If the audioTrack has been already been use, it needs to be reset before it can be played again
        audioTracks[index].stop()
        audioTracks[index].setPlaybackHeadPosition(0)

        audioTracks[index].play()
    }

    // Singleton
    companion object {
        @Volatile
        private var INSTANCE: AudioController? = null

        fun getController(): AudioController {
            // synchronized prevent multiple threads to create a different Controller instance
            return INSTANCE ?: synchronized(this) {
                val instance = AudioController()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}