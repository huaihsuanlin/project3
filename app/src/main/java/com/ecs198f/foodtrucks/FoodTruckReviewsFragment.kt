package com.ecs198f.foodtrucks

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FoodTruckReviewsFragment(private var args: FoodTruckDetailFragmentArgs) : Fragment() {
    lateinit var mGoogleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 9001

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_food_truck_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerViewAdapter = ReviewListRecyclerViewAdapter(listOf())
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("699423245763-insnbbp034ep600msiqfan5g0b2pau67.apps.googleusercontent.com")
            .requestEmail()
            .build()
        val account = GoogleSignIn.getLastSignedInAccount(view.context)
        updateUI(account)

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(view.context, gso)

        args.foodTruck.let {
            view.findViewById<RecyclerView>(R.id.reviewListRecyclerView).apply {
                adapter = recyclerViewAdapter
                layoutManager = LinearLayoutManager(context)
            }
            view.findViewById<Button>(R.id.signin_button).setOnClickListener{
                signin()
            }
            view.findViewById<Button>(R.id.enter_button).setOnClickListener {

                //TODO: post request
                postRequest(account!!, recyclerViewAdapter)

            }
            (requireActivity() as MainActivity).apply {

                foodTruckService.listReviews(it.id).enqueue(object : Callback<List<Review>> {
                    override fun onResponse(
                        call: Call<List<Review>>,
                        response: Response<List<Review>>
                    ) {
                        recyclerViewAdapter.updateReviews(response.body()!!)
                    }

                    override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                        throw t
                    }
                })
            }



        }
    }

    private fun postRequest(account: GoogleSignInAccount, recyclerViewAdapter: ReviewListRecyclerViewAdapter) {
        args.foodTruck.let {
            val input_content = view?.findViewById<TextInputEditText>(R.id.userInput)?.text
            val token = "Bearer " + account.idToken.toString()

            val review = Review(
                authorAvatarUrl = account.photoUrl.toString(),
                authorName = account.displayName.toString(),
                content = input_content.toString())
            Log.d("account name: ", account.displayName.toString())
            Log.d("account avatar: ", account.photoUrl.toString())
            Log.d("account content: ", input_content.toString())


            (requireActivity() as MainActivity).apply{
                foodTruckService.createFoodReview(token, it.id, review).enqueue(object : Callback<Unit>{
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        (requireActivity() as MainActivity).apply {

                            foodTruckService.listReviews(it.id).enqueue(object : Callback<List<Review>> {
                                override fun onResponse(
                                    call: Call<List<Review>>,
                                    response: Response<List<Review>>
                                ) {
                                    recyclerViewAdapter.updateReviews(response.body()!!)
                                    Log.d("TEST RESULT", "updated");
                                    Log.d("TEST RESULT", response.body()!!.toString());
                                }

                                override fun onFailure(call: Call<List<Review>>, t: Throwable) {
                                    throw t
                                }
                            })
                        }
                        Log.d("TEST RESULT", token)
                        Log.d("TEST RESULT", review.toString())
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        throw t
                    }

                })
            }
        }
    }


    private fun signin() {
        val signInIntent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode === RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e( "failed code=", e.statusCode.toString())
            updateUI(null)
        }
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if(account != null){ //  sign-in
            view?.findViewById<Button>(R.id.signin_button)?.visibility = View.INVISIBLE
            view?.findViewById<TextInputLayout>(R.id.textInputLayout)?.visibility = View.VISIBLE
            view?.findViewById<TextInputEditText>(R.id.userInput)?.visibility = View.VISIBLE

            Log.d("good", account.displayName.toString())

        }
        else{
            view?.findViewById<Button>(R.id.signin_button)?.visibility = View.VISIBLE
            view?.findViewById<TextInputLayout>(R.id.textInputLayout)?.visibility = View.INVISIBLE
            view?.findViewById<TextInputEditText>(R.id.userInput)?.visibility = View.INVISIBLE
            Log.d("warning", "need to sign in")
        }

    }


}

