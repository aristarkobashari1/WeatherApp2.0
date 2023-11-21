package com.example.feature.ui.profile

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.test.filters.SmallTest
import app.cash.turbine.test
import com.example.common.Language
import com.example.common.Units
import com.example.data.repo_impl.AppPreferencesRepository
import com.example.database.WeatherAppDatabase
import com.example.database.dao.WeatherDao
import com.example.feature.data.repo_impl.FakeWeatherRepositoryImpl
import com.example.model.PreferenceModel
import com.example.model.Profile
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@HiltAndroidTest
@SmallTest
@ExperimentalCoroutinesApi
class ProfileViewModelTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    @Named("test_dataStore")
    lateinit var dataStore: DataStore<Preferences>

    @Inject
    @Named("test_room")
    lateinit var testDatabase: WeatherAppDatabase

    private lateinit var testPreferencesRepository: AppPreferencesRepository
    private lateinit var testWeatherRepository: FakeWeatherRepositoryImpl
    private lateinit var testViewModel: ProfileViewModel
    private lateinit var testWeatherDao: WeatherDao

    @Before
    fun setUp(){
        hiltRule.inject()
        testWeatherDao = testDatabase.getWeatherDao()
        testPreferencesRepository = AppPreferencesRepository(dataStore)
        testWeatherRepository = FakeWeatherRepositoryImpl(testWeatherDao)
        testViewModel = ProfileViewModel(testWeatherRepository,testPreferencesRepository)
    }


    @Test
    fun profileData_should_return_data()= runTest{
        withContext(Dispatchers.IO){
            testViewModel.profileData.test {
                if(awaitItem()!=PreferenceModel()){
                    assertThat(awaitItem()).isEqualTo(PreferenceModel(
                        location = "test",
                        language = Language.ENG.lang,
                        unit = Units.METRIC.value,
                        profile = Profile(email="test@gmail.com", name = "test name", image = "image_uri") //if the values are not set
                    ))
                }
                else assertThat(awaitItem()).isEqualTo(PreferenceModel(profile = Profile("","","")))

            }
        }
    }


    @Test
    fun setLoggedUser_should_set_User() = runTest {
        withContext(Dispatchers.IO) {
            testViewModel.setLoggedUser(email = "test@gmail.com", name = "test name", userImage = "image_uri")
            testViewModel.loggedUser.test {
                assertThat(awaitItem()).isEqualTo(Profile("","",""))
                advanceTimeBy(1000)
                assertThat(awaitItem()).isEqualTo(Profile(email = "test@gmail.com", name = "test name", image = "image_uri"))
            }
        }
    }

    @Test
    fun signOut_should_clear_user() = runTest {
        withContext(Dispatchers.IO){
            testViewModel.signOut()
            testPreferencesRepository.getLoggedUser().test {
                assertThat(awaitItem()).isEqualTo(Profile("","",""))
            }
        }
    }


    @After
    fun tearDown() = runBlocking{
        testPreferencesRepository.clearDataStore()
        testDatabase.clearAllTables()
        testDatabase.close()
    }

}