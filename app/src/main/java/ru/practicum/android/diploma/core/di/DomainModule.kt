package ru.practicum.android.diploma.core.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import org.koin.dsl.module
import ru.practicum.android.diploma.favorites.domain.api.FavoriteInteractor
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.favorites.domain.impl.FavoriteInteractorImpl
import ru.practicum.android.diploma.filters.data.converter.FilterModelConverter
import ru.practicum.android.diploma.filters.data.repository.ChooseCountryRepositoryImpl
import ru.practicum.android.diploma.filters.domain.api.ChooseCountryInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseCountryRepository
import ru.practicum.android.diploma.filters.domain.api.ChooseIndustryInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseIndustryRepository
import ru.practicum.android.diploma.filters.domain.api.ChooseRegionInteractor
import ru.practicum.android.diploma.filters.domain.api.ChooseRegionRepository
import ru.practicum.android.diploma.filters.domain.api.FilterInteractor
import ru.practicum.android.diploma.filters.domain.api.FilterRepository
import ru.practicum.android.diploma.filters.domain.impl.ChooseCountryInteractorImpl
import ru.practicum.android.diploma.filters.domain.impl.ChooseIndustryInteractorImpl
import ru.practicum.android.diploma.filters.domain.impl.ChooseRegionsInteractorImpl
import ru.practicum.android.diploma.filters.domain.impl.FilterInteractorImpl
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.search.domain.impl.SearchInteractor
import ru.practicum.android.diploma.vacancy.domain.api.DetailVacancyRepository
import ru.practicum.android.diploma.vacancy.domain.api.SimilarVacancyRepository
import ru.practicum.android.diploma.vacancy.domain.impl.SimilarVacancyInteractor

@Module(includes = [DomainBindModule::class])
class DomainModule {
    @Provides
    fun provideSearchInteractor(searchRepository: SearchRepository): SearchInteractor {
        return SearchInteractor(
            searchRepository = searchRepository
        )
    }
    @Provides
    fun provideFilterInteractorImpl(
        filterRepository: FilterRepository,
        converter: FilterModelConverter
    ): FilterInteractorImpl {
        return FilterInteractorImpl(
            filterRepository = filterRepository,
            converter = converter,
        )
    }
    @Provides
    fun provideChooseCountryInteractorImpl(repository: ChooseCountryRepository): ChooseCountryInteractorImpl {
        return ChooseCountryInteractorImpl(
            repository = repository
        )
    }
    @Provides
    fun provideChooseRegionsInteractorImpl(repository: ChooseRegionRepository): ChooseRegionsInteractorImpl {
        return ChooseRegionsInteractorImpl(
            repository = repository
        )
    }
    @Provides
    fun provideChooseIndustryInteractorImpl(repository: ChooseIndustryRepository): ChooseIndustryInteractorImpl {
        return ChooseIndustryInteractorImpl(
            repository = repository
        )
    }
    @Provides
    fun provideSimilarVacancyInteractor(similarVacancyRepository: SimilarVacancyRepository): SimilarVacancyInteractor {
        return SimilarVacancyInteractor(
            similarVacancyRepository = similarVacancyRepository
        )
    }
    @Provides
    fun provideFavoriteInteractorImpl(
        favoriterepository: FavoriteVacancyRepository,
        detailVacancyRepository: DetailVacancyRepository
    ): FavoriteInteractorImpl {
        return FavoriteInteractorImpl(
            favoriterepository = favoriterepository,
            detailVacancyRepository = detailVacancyRepository,
        )
    }
}

@Module
interface DomainBindModule {

    @Suppress("FunctionName")
    @Binds
    fun bindFilterInteractorImpl_to_FilterInteractor(
        filterInteractorImpl: FilterInteractorImpl
    ) : FilterInteractor
    @Binds
    fun bindChooseCountryInteractorImpl_to_ChooseCountryInteractor(
        chooseCountryInteractorImpl: ChooseCountryInteractorImpl
    ) : ChooseCountryInteractor
    @Binds
    fun bindChooseRegionsInteractorImpl_to_ChooseRegionsInteractor(
        chooseRegionsInteractorImpl: ChooseRegionsInteractorImpl
    ) : ChooseRegionInteractor
    @Binds
    fun bindChooseIndustryInteractorImpl_to_ChooseIndustryInteractor(
        chooseIndustryInteractorImpl: ChooseIndustryInteractorImpl
    ) : ChooseIndustryInteractor
    @Binds
    fun bindFavoriteInteractorImpl_to_FavoriteInteractor(
        favoriteInteractorImpl: FavoriteInteractorImpl
    ) : FavoriteInteractor

}

/*val domainModule = module {
    factory {
        SearchInteractor(
            searchRepository = get()
        )
    }

    factory<FilterInteractor> {
        FilterInteractorImpl(
            filterRepository = get(),
            converter = get()
        )
    }
    factory<ChooseCountryInteractor> {
        ChooseCountryInteractorImpl(
            repository = get()
        )
    }
    factory<ChooseRegionInteractor> {
        ChooseRegionsInteractorImpl(
            repository = get()
        )
    }
    factory<ChooseIndustryInteractor> {
        ChooseIndustryInteractorImpl(
            repository = get()
        )
    }

    factory {
        SimilarVacancyInteractor(
            similarVacancyRepository = get()
        )
    }

    factory<FavoriteInteractor> {
        FavoriteInteractorImpl(
            favoriterepository = get(),
            detailVacancyRepository = get(),
        )
    }
}*/