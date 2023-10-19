package ru.practicum.android.diploma.team

import android.view.LayoutInflater
import android.view.ViewGroup
import ru.practicum.android.diploma.common.utils.BindingFragment
import ru.practicum.android.diploma.databinding.FragmentTeamBinding

class TeamFragment : BindingFragment<FragmentTeamBinding>() {

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentTeamBinding {
        return FragmentTeamBinding.inflate(inflater, container, false)
    }
}