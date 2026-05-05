package com.example.studentcontactapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentcontactapp.adapter.StudentAdapter
import com.example.studentcontactapp.database.AppDatabase
import com.example.studentcontactapp.database.dao.StudentDao
import kotlinx.coroutines.launch

class SearchFragment : Fragment() {

    private lateinit var studentDao: StudentDao
    private lateinit var adapter: StudentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = AppDatabase.getDatabase(requireContext())
        studentDao = database.studentDao()

        val etSearch = view.findViewById<EditText>(R.id.etSearch)
        val rvSearch = view.findViewById<RecyclerView>(R.id.rvSearch)

        adapter = StudentAdapter(
            onItemClick = { student ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra("EXTRA_NAME", student.name)
                intent.putExtra("EXTRA_NIM", student.nim)
                intent.putExtra("EXTRA_PRODI", student.prodi)
                startActivity(intent)
            },
            onEditClick = { student ->
                val intent = Intent(requireContext(), FormStudentActivity::class.java)
                intent.putExtra("EXTRA_ID", student.id)
                intent.putExtra("EXTRA_NAME", student.name)
                intent.putExtra("EXTRA_NIM", student.nim)
                intent.putExtra("EXTRA_PRODI", student.prodi)
                intent.putExtra("EXTRA_EMAIL", student.email)
                intent.putExtra("EXTRA_SEMESTER", student.semester)
                startActivity(intent)
            },
            onDeleteClick = { student ->
                showDeleteConfirmationDialog(student.id, student.name)
            }
        )

        rvSearch.layoutManager = LinearLayoutManager(requireContext())
        rvSearch.adapter = adapter

        performSearch("")

        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                performSearch(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun performSearch(keyword: String) {
        lifecycleScope.launch {
            studentDao.searchStudents(keyword).collect { studentList ->
                adapter.setData(studentList)
            }
        }
    }

    private fun showDeleteConfirmationDialog(studentId: Int, studentName: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Hapus Data?")
        builder.setMessage("Hapus \"$studentName\"? Tindakan ini tidak dapat dibatalkan.")

        builder.setPositiveButton("Hapus") { dialog, _ ->
            lifecycleScope.launch {
                studentDao.deleteById(studentId)
                Toast.makeText(requireContext(), "$studentName dihapus", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Batal") { dialog, _ -> dialog.dismiss() }
        builder.create().show()
    }
}