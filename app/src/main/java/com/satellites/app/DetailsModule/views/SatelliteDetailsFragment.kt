package com.satellites.app.DetailsModule.views

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.satellites.app.DetailsModule.viewModel.DetailsViewModel
import com.satellites.app.R
import com.satellites.app.base.SatelliteApplication
import com.satellites.app.constants.Constants.Companion.SATELLITE
import com.satellites.app.entities.Details
import com.satellites.app.entities.Position
import com.satellites.app.entities.Satellite
import com.satellites.app.utils.Utilities
import kotlinx.android.synthetic.main.fragment_satellite_details.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.String
import java.lang.reflect.Type


class SatelliteDetailsFragment : Fragment() {
    private var countDownTimer: CountDownTimer? = null
    var satellite: Satellite? = null
    var positionInforemainingTime: Long = 9000L
    private val detailsViewModel: DetailsViewModel by viewModels {
        DetailsViewModel.DetailsViewModelFactory((activity?.application as SatelliteApplication).repository)
    }

    fun SatelliteDetailsFragment() {
        // Required empty public constructor
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            satellite =
                Gson().fromJson(requireArguments().getString(SATELLITE), Satellite::class.java)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        progress_circular.visibility = View.VISIBLE
        if (satellite != null) {
            nameTv.text = satellite?.name
            var details = detailsViewModel.getDetailsBySatelliteId(satellite?.id!!)
            var satellitePositionsList = detailsViewModel.getPositionsBySatelliteId(satellite?.id!!)
            if (details != null) {
                displayDetails(details, satellitePositionsList)
            } else {
                val builder = GsonBuilder()
                val gson: Gson = builder.create()

                val inputStream = resources.openRawResource(R.raw.satellite_detail)
                val jsonStr = Utilities.streamToString(inputStream)

                try {
                    val jsonArray = JSONArray(jsonStr)
                    val type: Type = object : TypeToken<List<Details?>?>() {}.getType()
                    val jsonResults: List<Details> = gson.fromJson(jsonArray.toString(), type)
                    for (i in jsonResults.indices) {
                        Log.e("JSON $i", jsonResults[i].first_flight.toString())
                        if (jsonResults[i].id == satellite?.id) {
                            detailsViewModel.insertDetails(jsonResults[i])
                            details?.id = satellite?.id
                            details = jsonResults[i]
                            break
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }


                val inputStream2 = resources.openRawResource(R.raw.positions)
                val jsonStr2 = Utilities.streamToString(inputStream2)

                try {
                    val jsonObject = JSONObject(jsonStr2)
                    val jsonArray = jsonObject.getJSONArray("list")
                    for (i in 0 until jsonArray.length()) {
                        val satellitePositionsObject = jsonArray.getJSONObject(i)
                        val satelliteId = satellitePositionsObject.getInt("id")
                        val satellitePositionsJsonList =
                            satellitePositionsObject.getJSONArray("positions")
                        if (satelliteId == satellite?.id) {
                            val type: Type = object : TypeToken<List<Position?>?>() {}.getType()
                            val jsonResults: List<Position> =
                                gson.fromJson(satellitePositionsJsonList.toString(), type)
                            for (i in jsonResults.indices) {
                                jsonResults[i].id = satellite?.id
                                detailsViewModel.insertPosition(jsonResults[i])
                                Log.e("JSON $i", jsonResults[i].id.toString())
                                Log.e("JSON $i", jsonResults[i].posX.toString())
                                Log.e("JSON $i", jsonResults[i].posY.toString())
                            }

                            satellitePositionsList = jsonResults
                        }
                    }


                    displayDetails(details!!, satellitePositionsList)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        }


    }

    private fun displayDetails(details: Details, positionList: List<Position>) {
        dateTv.text = details.first_flight
        massHeightTv.text = details.height.toString() + "/" + details.mass.toString()
        costTv.text = details.cost_per_launch.toString()
        startUpdatePositionTimer(positionList)
        progress_circular.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_satellite_details, container, false)

        return view
    }


    private fun startUpdatePositionTimer(positionList: List<Position>) {
        countDownTimer = object : CountDownTimer(positionInforemainingTime, 1000) {
            override fun onTick(remainingTime: Long) {

                val value: Long = (positionInforemainingTime / 3).toLong()
                if (!positionList.isEmpty() && positionList.size >= 3)
                    if (remainingTime <= value * 3 && remainingTime >= (value * 2) + 1) {
                        lastPositionTv.setText(
                            String.format(
                                "(%s.%s)", positionList.get(0).posX, positionList.get(0).posY

                            )
                        )

                    } else if (remainingTime <= value * 2 && remainingTime >= value + 1) {
                        lastPositionTv.setText(
                            String.format(
                                "(%s.%s)", positionList.get(1).posX, positionList.get(1).posY

                            )
                        )
                    } else if (remainingTime <= value && remainingTime >= 0) {
                        lastPositionTv.setText(
                            String.format(
                                "(%s.%s)", positionList.get(2).posX, positionList.get(2).posY

                            )
                        )

                    }
            }

            override fun onFinish() {
                startUpdatePositionTimer(positionList)
            }
        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if (countDownTimer != null)
            countDownTimer?.cancel()
    }

}
