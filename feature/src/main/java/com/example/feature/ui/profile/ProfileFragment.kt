package com.example.feature.ui.profile

import android.app.Activity
import android.net.Uri
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
import com.example.common.configUnits
import com.example.feature.databinding.FragmentProfileBinding
import com.example.feature.util.observeNavigation
import com.example.model.PreferenceModel
import com.example.model.Profile
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
            repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    viewModel.profileData.collectLatest {prefModel->
                        if(prefModel!= PreferenceModel()){
                            viewBinding.apply {
                                location = prefModel.location
                                unit = prefModel.unit
                                language = prefModel.language
                                profile = if(prefModel.profile?.isProfileEmpty() == false) prefModel.profile else Profile("Not Signed in")
                            }

                            val units = prefModel.unit.takeIf { it.isNotEmpty() }?.configUnits()
                            viewBinding.tempUnit = units?.first
                            viewBinding.speedUnit = units?.second

                            viewModel.initSignIn.update { prefModel.profile?.isProfileEmpty() == true }
                        }

                    }
                }

                launch {
                    viewModel.initSignIn.collectLatest { shouldInitSignIn->
                        viewBinding.logOutText.text = if (shouldInitSignIn) "Log in" else "Log out"
                        viewBinding.logOutText.setOnClickListener {
                            if (shouldInitSignIn) displaySignIn() else {
                                oneTapClient.signOut()
                                viewModel.signOut()
                            }
                        }

                    }
                }
            }
        }
    }

    private fun displaySignIn(){
//        oneTapClient.beginSignIn(signInRequest)
//            .addOnSuccessListener(requireActivity() as HomeActivity) { result ->
//                resultFlag = Google.RC_SIGN_IN
//                val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
//                activityResultLauncher.launch(intentSenderRequest)
//            }
//            .addOnFailureListener(requireActivity() as HomeActivity) { e ->
//                requireContext().makeToastShort(e.localizedMessage)
//            }
        viewModel.setLoggedUser("aristarko@gmail.com","Aristarko", Uri.parse("https://assets.materialup.com/uploads/039c280b-4cf2-4188-9c11-5149971666dc/preview.png").toString())

    }

    private fun handleResultLauncher(){
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                try {
                    when(resultFlag){
                        Google.RC_SIGN_IN -> {
                            val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                            viewModel.setLoggedUser(credential.id,credential.givenName!!,credential.profilePictureUri.toString())
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