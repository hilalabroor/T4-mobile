package com.example.studentcontactapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.studentcontactapp.adapter.StudentAdapter
import com.example.studentcontactapp.database.AppDatabase
import com.example.studentcontactapp.database.dao.StudentDao
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private lateinit var studentDao: StudentDao
    private lateinit var adapter: StudentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = AppDatabase.getDatabase(requireContext())
        studentDao = database.studentDao()

        val rvStudents = view.findViewById<RecyclerView>(R.id.rvStudents)
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fabAdd)

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

        rvStudents.layoutManager = LinearLayoutManager(requireContext())
        rvStudents.adapter = adapter

        val itemTouchHelperCallback = object : androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(
            0, androidx.recyclerview.widget.ItemTouchHelper.LEFT or androidx.recyclerview.widget.ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val studentToDelete = adapter.getStudentAt(position)

                lifecycleScope.launch {
                    studentDao.deleteById(studentToDelete.id)
                    Toast.makeText(requireContext(), "${studentToDelete.name} dihapus", Toast.LENGTH_SHORT).show()
                }
            }
        }

        val itemTouchHelper = androidx.recyclerview.widget.ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(rvStudents)

        lifecycleScope.launch {
            studentDao.getAllStudents().collect { studentList ->
                adapter.setData(studentList)
            }
        }

        fabAdd.setOnClickListener {
            val intent = Intent(requireContext(), FormStudentActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showDeleteConfirmationDialog(studentId: Int, studentName: String) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Hapus Data?")
        builder.setMessage("Hapus \"$studentName\"? Tindakan ini tidak dapat dibatalkan.")

        builder.setPositiveButton("Hapus") { dialog, _ ->
            lifecycleScope.launch {
                studentDao.deleteById(studentId)
                Toast.makeText(requireContext(), "$studentName berhasil dihapus", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Batal") { dialog, _ ->
            dialog.dismiss() // Tutup pop-up jika batal
        }

        val dialog = builder.create()
        dialog.show()
    }
}