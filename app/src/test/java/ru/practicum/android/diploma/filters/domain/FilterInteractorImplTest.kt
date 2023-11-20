package ru.practicum.android.diploma.filters.domain

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import ru.practicum.android.diploma.filters.data.converter.FilterModelConverter
import ru.practicum.android.diploma.filters.domain.api.FilterRepository
import ru.practicum.android.diploma.filters.domain.impl.FilterInteractorImpl

class FilterInteractorImplTest {

    private val filterRepository = mock<FilterRepository>()
    private val converter = mock<FilterModelConverter>()
    @Test
    fun `should return the same country as in repository`(){
        val testCountry = "Russia"
        Mockito.`when`(filterRepository.getCountry()).thenReturn(testCountry)

        val filterInteractor = FilterInteractorImpl(filterRepository, converter)
        val actual = filterInteractor.getCountry()
        val expected = "Russia"

        Assertions.assertEquals(expected, actual)
    }

    @Test
    fun `should return the same region as in repository`(){
        val testRegion = "Saint-Petersburg"
        Mockito.`when`(filterRepository.getRegion()).thenReturn(testRegion)

        val filterInteractor = FilterInteractorImpl(filterRepository, converter)
        val actual = filterInteractor.getRegion()
        val expected = "Saint-Petersburg"

        Assertions.assertEquals(expected, actual)
    }
}