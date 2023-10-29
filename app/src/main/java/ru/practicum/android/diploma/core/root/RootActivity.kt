package ru.practicum.android.diploma.core.root

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {
    private var binding: ActivityRootBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding?.bottomNavigationView?.setupWithNavController(navController)

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
                if (destination.id in hideBottomNavForDestinations) {
                    binding?.topBorder?.isVisible = false
                    binding?.bottomNavigationView?.isVisible = false
                } else {
                    binding?.topBorder?.isVisible = true
                    binding?.bottomNavigationView?.isVisible = true
                }
        }
    }

    fun animateBottomNavigationView() {
        binding?.bottomNavigationView?.visibility = View.GONE
    }
    
    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}