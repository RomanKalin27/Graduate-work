package ru.practicum.android.diploma.core.root

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
//import org.koin.android.BuildConfig
//import ru.practicum.android.diploma.BuildConfig
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRootBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)

        //Скрытие bottomNav
        val hideBottomNavForDestinations = setOf(
            R.id.chooseCountryFragment,
            R.id.filtersFragment,
            R.id.choosePlaceWorkFragment,
            R.id.chooseRegionFragment,
            R.id.chooseIndustry,
            R.id.vacancyFragment,
            R.id.similarVacancyFragment
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigationView.visibility =
                if (destination.id in hideBottomNavForDestinations) {
                    View.GONE
                } else {
                    View.VISIBLE
                }
        }

        // Пример использования access token для HeadHunter API
//        networkRequestExample(accessToken = BuildConfig.HH_ACCESS_TOKEN)
    }

    fun animateBottomNavigationView() {
        binding.bottomNavigationView.visibility = View.GONE
    }

    private fun networkRequestExample(accessToken: String) {
        // ...
    }

}