package com.mobdeve.agbuya.hallar.hong.fridge.fragment

import android.os.Bundle
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
    private var listener: OnRecipeClickListener? = null

    interface OnRecipeClickListener {
        fun onRecipeSelected(recipe: RecipeModel)
    }

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
        }
        listener = activity as? OnRecipeClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reusable_recycler_view, container, false)
        recyclerView = view.findViewById(R.id.addedIngredientsRv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = RecipeMainAdapter(
            recipeList = recipeList,
            onRecipeClick = { selectedRecipe ->
                listener?.onRecipeSelected(selectedRecipe)
            }
        )
        recyclerView.adapter = adapter

        return view
    }
}