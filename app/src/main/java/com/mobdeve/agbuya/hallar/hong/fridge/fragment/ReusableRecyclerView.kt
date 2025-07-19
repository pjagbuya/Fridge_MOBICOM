package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.agbuya.hallar.hong.fridge.R
import com.mobdeve.agbuya.hallar.hong.fridge.adapter.RecipeMainAdapter
import com.mobdeve.agbuya.hallar.hong.fridge.domain.RecipeModel

class ReusableRecyclerView : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecipeMainAdapter
    private var recipeList = ArrayList<RecipeModel>()

    companion object {
        private const val ARG_RECIPES = "ARG_RECIPES"

        fun newInstance(recipes: ArrayList<RecipeModel>): ReusableRecyclerView {
            val fragment = ReusableRecyclerView()
            val bundle = Bundle()
            bundle.putParcelableArrayList(ARG_RECIPES, recipes)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            recipeList = it.getParcelableArrayList(ARG_RECIPES) ?: ArrayList()
            Log.d("ReusableRecyclerView", "Received ${recipeList.size} recipes")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reusable_recycler_view, container, false)

        Log.d("Reusable----recipe", "Recipe List Size: ${recipeList.size}")
        recipeList.forEach { Log.d("RecipeActivity", "Recipe: ${it.name}") }
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        recipeList = arguments?.getParcelableArrayList(ARG_RECIPES) ?: arrayListOf()
        Log.d("ReusableRecyclerView", "Received recipeList size: ${recipeList.size}")
        adapter = RecipeMainAdapter(recipeList)
        recyclerView.adapter = adapter

        return view
    }
}
