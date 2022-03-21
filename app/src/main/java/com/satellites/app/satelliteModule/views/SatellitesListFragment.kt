package com.satellites.app.satelliteModule.views

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.satellites.app.DetailsModule.views.SatelliteDetailsFragment
import com.satellites.app.R
import com.satellites.app.base.SatelliteApplication
import com.satellites.app.constants.Constants
import com.satellites.app.entities.Satellite
import com.satellites.app.satelliteModule.adapter.SatellitesAdapter
import com.satellites.app.satelliteModule.viewModel.SatelliteViewModel
import com.satellites.app.satelliteModule.viewModel.SatelliteViewModelFactory
import com.satellites.app.utils.FragmentUtils
import com.satellites.app.utils.Utilities
import org.json.JSONArray
import org.json.JSONException
import java.lang.reflect.Type


class SatellitesListFragment : Fragment(), SatellitesAdapter.SatelliteCardInteraction {
    val adapter: SatellitesAdapter = SatellitesAdapter()
    private var recycler_view: RecyclerView? = null
    private var searchView: SearchView? = null
    var satelliteList = ArrayList<Satellite>()
    private val satelliteViewModel: SatelliteViewModel by viewModels {
        SatelliteViewModelFactory((activity?.application as SatelliteApplication).repository)
    }

    fun SatellitesListFragment() {
        // Required empty public constructor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val builder = GsonBuilder()
        val gson: Gson = builder.create()

        val inputStream = resources.openRawResource(R.raw.satellite_list)
        val jsonStr = Utilities.streamToString(inputStream)

        try {
            val jsonArray = JSONArray(jsonStr)
            val type: Type = object : TypeToken<List<Satellite?>?>() {}.getType()
            val jsonResults: List<Satellite> = gson.fromJson(jsonArray.toString(), type)
            for (i in jsonResults.indices) {
                Log.e("JSON $i", jsonResults[i].name.toString())
                satelliteList.add(jsonResults[i])
                satelliteViewModel.insertSatellite(jsonResults[i])
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }

    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        satelliteViewModel.allSatellite.observe(activity as MainActivity) { satellite ->
            // Update the cached copy of the words in the adapter.
            satellite.let {
                if (it.size == 0) {
                    adapter.setList(satelliteList)
                    adapter.submitList(satelliteList)
                } else {
                    adapter.setList(it)
                    adapter.submitList(it)
                }
                 }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_satellites_list, container, false)
        recycler_view = view.findViewById(R.id.satelliteRV)
        searchView = view.findViewById(R.id.satelliteSV)
        recycler_view?.adapter = adapter
        adapter.interaction = this
        recycler_view?.layoutManager = LinearLayoutManager(context)


        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                adapter?.getFilter()?.filter(s)
                return false
            }
        })
        return view
    }

    override fun onSatelliteClicked(satellite: Satellite) {
        val bundle = Bundle()
        bundle.putString(Constants.SATELLITE, Gson().toJson(satellite))
        val satelliteFragment = SatelliteDetailsFragment()
        satelliteFragment.arguments = bundle
        FragmentUtils.launchFragmentWithDefaultAnimation(
            requireActivity(),
            R.id.container_fragment,
            satelliteFragment,
            FragmentUtils.FragmentLaunchMode.ADD,
            true
        )
    }

}
