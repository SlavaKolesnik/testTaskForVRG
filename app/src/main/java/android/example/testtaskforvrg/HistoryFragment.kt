package android.example.testtaskforvrg

import android.content.Intent
import android.content.res.Configuration
import android.example.testtaskforvrg.adapter.HistoryAdapter
import android.example.testtaskforvrg.constance.Constance
import android.example.testtaskforvrg.databinding.FragmentHistoryBinding
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HistoryFragment : Fragment() {

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private lateinit var db: MainDB

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        db = MainDB.getDb(requireContext())

        binding.clearHistory.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                db.getDao().deleteAllItems()
                withContext(Dispatchers.Main) {
                    loadSavedImages()
                }
            }
        }

        setupRecyclerView()
        loadSavedImages()
        return binding.root
    }

    private fun setupRecyclerView() {
        val orientation = resources.configuration.orientation
        val numberOfColumns = if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            4
        } else {
            2
        }
        binding.rcHistory.layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
        adapter = HistoryAdapter(emptyList()) { item ->

            val intent = Intent(requireContext(), ItemActivity::class.java).apply {
                putExtra(Constance.KEY_1, item.pict)
            }
            startActivity(intent)
        }
        binding.rcHistory.adapter = adapter
    }

    private fun loadSavedImages() {
        lifecycleScope.launch {
            db.getDao().getAllItem().collect { items ->
                adapter = HistoryAdapter(items) { item ->

                    val intent = Intent(requireContext(), ItemActivity::class.java).apply {
                        putExtra(Constance.KEY_1, item.pict)
                    }
                    startActivity(intent)
                }
                binding.rcHistory.adapter = adapter
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = HistoryFragment()
    }
}