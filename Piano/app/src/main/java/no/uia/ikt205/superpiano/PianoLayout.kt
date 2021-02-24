package no.uia.ikt205.superpiano

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.fragment_full_tone_piano_key.*
import kotlinx.android.synthetic.main.fragment_full_tone_piano_key.view.*
import kotlinx.android.synthetic.main.fragment_piano.*
import kotlinx.android.synthetic.main.fragment_piano.view.*
import no.uia.ikt205.superpiano.data.Note
import no.uia.ikt205.superpiano.databinding.FragmentPianoBinding
import java.io.File
import java.io.FileOutputStream
import kotlin.collections.*


class PianoLayout : Fragment() {

    private var _binding:FragmentPianoBinding? = null
    private val binding get() = _binding!!

    private val fullTones = listOf("C","D","E","F","G","A","B","C2","D2","E2","F2","G2")

    private val halfTones = listOf("C#", "D#", "F#", "G#", "A#", "C#2", "D#2", "F#2", "G#2", "A#2")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        _binding = FragmentPianoBinding.inflate(layoutInflater)
        val view = binding.root

        val fm = childFragmentManager
        val ft = fm.beginTransaction()

        fullTones.forEach { orgNoteValue ->
            val fullTonePianoKey = FullTonePianoKeyFragment.newInstance(orgNoteValue)
            var startPlay:Long = 0

            fullTonePianoKey.onKeyDown =  {
                println("Piano key down $it")
            }

            fullTonePianoKey.onKeyUp = {
                println("Piano key up $it")
            }

            ft.add(view.pianoKeys.id,fullTonePianoKey,"note_$orgNoteValue")
        }

        halfTones.forEach { orgNoteValue ->
            val fullTonePianoKey = HalfTonePianoKeyFragment.newInstance(orgNoteValue)
            var startPlay:Long = 0

            fullTonePianoKey.onKeyPressed =  {
                println("Piano key down $it")
            }

            fullTonePianoKey.onKeyReleased = {
                println("Piano key up $it")
            }

            ft.add(view.pianoKeys.id,fullTonePianoKey,"note_$orgNoteValue")
        }

        ft.commit()

        return view
    }

}
