package com.example.feature.ui.profile

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.common.Google
import com.example.common.makeToastShort
import com.example.feature.HomeActivity
import com.example.feature.databinding.FragmentProfileBinding
import com.example.feature.util.observeNavigation
import com.example.model.PreferenceModel
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class Profile : Fragment() {

    @Inject
    lateinit var oneTapClient: SignInClient
    @Inject
    lateinit var signInRequest: BeginSignInRequest

    private lateinit var viewBinding: FragmentProfileBinding
    private val viewModel: ProfileViewModel by activityViewModels()
    private lateinit var activityResultLauncher: ActivityResultLauncher<IntentSenderRequest>
    private var resultFlag: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentProfileBinding.inflate(layoutInflater)
        return viewBinding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeNavigation(viewModel)
        initFlow()
        handleResultLauncher()
    }

    private fun initFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.profileData.collectLatest {
                        if(it!= PreferenceModel()){
                            viewBinding.apply {
                                location = it.location
                                unit = it.unit
                                language = it.language
                                profile = it.profile
                            }
                            if(it.profile?.isProfileEmpty() == true)
                                viewModel.initSignIn.update { false }
                        }

                    }
                }

                launch {
                    viewModel.initSignIn.collectLatest { shouldInitSignIn->
                        viewBinding.logOutText.text = if (shouldInitSignIn) "Log in" else "Log out"
                        viewBinding.logOutText.setOnClickListener {
                            if (shouldInitSignIn) displaySignIn() else viewModel.signOut()
                        }

                    }
                }
            }
        }
    }

    private fun displaySignIn(){
        oneTapClient.beginSignIn(signInRequest)
            .addOnSuccessListener(requireActivity() as HomeActivity) { result ->
                resultFlag = Google.RC_SIGN_IN
                val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                activityResultLauncher.launch(intentSenderRequest)
            }
            .addOnFailureListener(requireActivity() as HomeActivity) { e ->
                requireContext().makeToastShort(e.localizedMessage)
            }
    }

    private fun handleResultLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    when(resultFlag){
                        Google.RC_SIGN_IN -> {
                            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                            requireContext().makeToastShort(credential.id)
                            viewModel.setLoggedUser(credential.id,credential.givenName!!)
                        }
                        else ->{}
                    }

                } catch (e: ApiException) {
                    Log.e("TAG", e.toString())
                }
            } else {
                // Handle failure or cancellation
                // You can add your error handling logic here
            }
        }
    }
}