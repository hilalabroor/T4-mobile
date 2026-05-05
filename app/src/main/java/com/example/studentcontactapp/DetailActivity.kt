package com.example.studentcontactapp

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.FileNotFoundException

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val tvBack = findViewById<TextView>(R.id.tvBack)
        val tvDetailAvatar = findViewById<TextView>(R.id.tvDetailAvatar)
        val tvDetailName = findViewById<TextView>(R.id.tvDetailName)
        val tvDetailNimProdi = findViewById<TextView>(R.id.tvDetailNimProdi)
        val etNote = findViewById<EditText>(R.id.etNote)
        val btnSaveNote = findViewById<Button>(R.id.btnSaveNote)
        val btnLoadNote = findViewById<Button>(R.id.btnLoadNote)
        val tvNoteStatus = findViewById<TextView>(R.id.tvNoteStatus)

        val name = intent.getStringExtra("EXTRA_NAME") ?: "Tanpa Nama"
        val nim = intent.getStringExtra("EXTRA_NIM") ?: "0000000"
        val prodi = intent.getStringExtra("EXTRA_PRODI") ?: "-"

        tvDetailName.text = name
        tvDetailNimProdi.text = "$nim - $prodi"

        val initials = name.split(" ")
            .mapNotNull { it.firstOrNull()?.toString() }
            .take(2).joinToString("").uppercase()
        tvDetailAvatar.text = if (initials.isNotEmpty()) initials else "?"

        tvBack.setOnClickListener {
            finish()
        }

        val noteFileName = "catatan_$nim.txt"

        btnSaveNote.setOnClickListener {
            val noteContent = etNote.text.toString()
            try {
                openFileOutput(noteFileName, Context.MODE_PRIVATE).use {
                    it.write(noteContent.toByteArray())
                }

                tvNoteStatus.visibility = View.VISIBLE
                tvNoteStatus.text = "✓ Tersimpan (${noteContent.toByteArray().size} bytes)"
                Toast.makeText(this, "Catatan disimpan", Toast.LENGTH_SHORT).show()

            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Gagal menyimpan catatan", Toast.LENGTH_SHORT).show()
            }
        }

        btnLoadNote.setOnClickListener {
            try {
                val fileInputStream = openFileInput(noteFileName)
                val content = fileInputStream.bufferedReader().use { it.readText() }

                etNote.setText(content)
                tvNoteStatus.visibility = View.VISIBLE
                tvNoteStatus.text = "✓ Dimuat (${content.toByteArray().size} bytes)"
                Toast.makeText(this, "Catatan dimuat", Toast.LENGTH_SHORT).show()

            } catch (e: FileNotFoundException) {
                Toast.makeText(this, "Belum ada catatan", Toast.LENGTH_SHORT).show()
                etNote.setText("")
                tvNoteStatus.visibility = View.GONE
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Gagal memuat catatan", Toast.LENGTH_SHORT).show()
            }
        }
    }
}