package com.example.secondapp.fragments.indemnify


import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.secondapp.AppConfig
import com.example.secondapp.R
import com.example.secondapp.VinalifeActivity
import com.example.secondapp.adapters.BankAdapter
import com.example.secondapp.data.BaseResponseObject
import com.example.secondapp.data.auth.AuthClient
import com.example.secondapp.data_local.SharedPrefs
import com.example.secondapp.model.Bank
import kotlinx.android.synthetic.main.fragment_indemnify.view.*
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class IndemnifyFragment (private val vinalifeActivity: VinalifeActivity) : Fragment() {

    lateinit var option :Spinner
    private val client: AuthClient = AppConfig.retrofit.create(AuthClient::class.java)
    var requestType:String="ME"
    lateinit var bankAdapter: BankAdapter
    private var listBank = arrayListOf<Bank>()
    var bankIdSelected = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_indemnify, container, false)
        view.indemnify_description.setText(Html.fromHtml((SharedPrefs.instance.getObject("indemnify_setup").get("description").toString()).replace("\n","<br>")))

        val toolbar: Toolbar = view.findViewById(R.id.indemnify_toolbar)
        toolbar.setNavigationIcon(R.drawable.ic_baseline_chevron_left_24)
        toolbar.setNavigationOnClickListener(View.OnClickListener { requireActivity().onBackPressed() })

        val bundle = this.arguments
        if (bundle != null) {
            if(bundle.getString("type_request")!=null) requestType=bundle.getString("type_request").toString()
        }

        bankAdapter = BankAdapter(requireContext(),R.layout.item_dropdown_selected,listBank())
        view.indemnify_bank_id_spinner.setAdapter(bankAdapter)

        option = view.findViewById(R.id.indemnify_bank_id_spinner) as Spinner

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                bankIdSelected = listBank[position].id
                Toast.makeText(requireContext(),listBank[position].name,Toast.LENGTH_LONG).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                //
            }
        }

//        view.indemnify_bank_id_spinner.onItemSelectedListener(object : AdapterView.OnItemSelectedListener{
//            override fun onItemSelected(
//                parent: AdapterView<*>?,
//                view: View?,
//                position: Int,
//                id: Long
//            ) {
//                TODO("Not yet implemented")
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                TODO("Not yet implemented")
//            }
//        })

        // Inflate the layout for this fragment
        return view
    }

    private fun listBank(): ArrayList<Bank> {
        listBank = arrayListOf<Bank>()
        listBank.add(Bank("Vui lòng chọn!",0))
        val service: Call<BaseResponseObject> = client.listBank()
        service.enqueue(object: Callback<BaseResponseObject> {
            override fun onResponse(call: Call<BaseResponseObject>, response: Response<BaseResponseObject>) {

                if(response.isSuccessful){
                    (activity as VinalifeActivity).closeLoading()
                    val body = response.body()

                    if(body?.status!=200){
                        Toast.makeText(context,response.body()?.content, Toast.LENGTH_LONG).show()
                        return
                    }

                    val data = body.data

                    val listString = data?.get("list").toString()

                    if(listString!=""){
                        val arrayData = JSONTokener(listString).nextValue() as JSONArray

                        for (i in 0.. arrayData.length() - 1){
                            val item = arrayData[i].toString()

                            val jsonItem = JSONTokener(item).nextValue() as JSONObject
                            val name = jsonItem.get("BankName").toString().trim('"')
                            val id = jsonItem.get("BankID").toString().trim('"').toInt()
                            listBank.add(Bank(name,id))
                        }


                    }
//
//                    if(others!=null){
//                        val jsonArray = Gson().fromJson(others, mutableListOf<String>().javaClass)
//
//                        parseList(jsonArray,view)
//                        //println(jsonArray[0])
//
//                    }



//                    val userId = data?.get("user_id").toString()
//                    val mobile_number = data?.get("mobile").toString()
//                    val userOid = data?.get("user_oid").toString()
//                    val my_referral_code = data?.get("my_referral_code").toString()
//                    val token = data?.get("token").toString()
//                    val shortToken = data?.get("short_token").toString()
//                    val auth = Auth(0,Integer.parseInt(userId),mobile_number,userOid,my_referral_code,token,shortToken)
//
//
//
//                    db.authDao().addData(auth)
//                    println(db.authDao().getAll()[0].user_id)
//                    val myIntent = Intent(activity, VinalifeActivity::class.java)
//                    startActivity(myIntent)

                }

            }

            override fun onFailure(call: Call<BaseResponseObject>, t: Throwable) {
                Toast.makeText(activity,t.toString(), Toast.LENGTH_SHORT).show()
            }

        })
        return listBank
    }

    private fun setListBank(){

    }


}