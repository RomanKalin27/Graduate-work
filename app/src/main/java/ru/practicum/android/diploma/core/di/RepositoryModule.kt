package ru.practicum.android.diploma.core.di

import android.content.SharedPreferences
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.practicum.android.diploma.db.data.impl.VacancyDbRepositoryImpl
import ru.practicum.android.diploma.db.domain.AppDB
import ru.practicum.android.diploma.db.domain.api.VacancyDbRepository
import ru.practicum.android.diploma.favorites.data.FavoriteRepositoryImpl
import ru.practicum.android.diploma.favorites.domain.api.FavoriteVacancyRepository
import ru.practicum.android.diploma.filters.data.converter.FilterModelConverter
import ru.practicum.android.diploma.filters.data.repository.ChooseCountryRepositoryImpl
import ru.practicum.android.diploma.filters.data.repository.ChooseIndustryRepositoryImpl
import ru.practicum.android.diploma.filters.data.repository.ChooseRegionRepositoryImpl
import ru.practicum.android.diploma.filters.data.repository.FilterRepositoryImpl
import ru.practicum.android.diploma.filters.domain.api.ChooseCountryRepository
import ru.practicum.android.diploma.filters.domain.api.ChooseIndustryRepository
import ru.practicum.android.diploma.filters.domain.api.ChooseRegionRepository
import ru.practicum.android.diploma.filters.domain.api.FilterInteractor
import ru.practicum.android.diploma.filters.domain.api.FilterRepository
import ru.practicum.android.diploma.filters.domain.impl.FilterInteractorImpl
import ru.practicum.android.diploma.search.data.impl.SearchRepositoryImpl
import ru.practicum.android.diploma.search.data.network.ApiService
import ru.practicum.android.diploma.search.data.network.ConnectivityHelper
import ru.practicum.android.diploma.search.data.network.ModelConverter
import ru.practicum.android.diploma.search.domain.api.SearchRepository
import ru.practicum.android.diploma.vacancy.data.impl.DetailVacancyRepositoryImpl
import ru.practicum.android.diploma.vacancy.data.impl.SimilarVacancyRepositoryImpl
import ru.practicum.android.diploma.vacancy.domain.api.DetailVacancyRepository
import ru.practicum.android.diploma.vacancy.domain.api.SimilarVacancyRepository

@Module(includes = [RepositoryBindModule::class])
class RepositoryModule {
    @Provides
    fun provideSearchRepository(
        apiService: ApiService,
        networkControl: ConnectivityHelper,
        converter: ModelConverter,
        sharedPreferences: SharedPreferences
    ): SearchRepository {
        return SearchRepositoryImpl(
            apiService = apiService,
            networkControl = networkControl,
            converter = converter,
            sharedPreferences = sharedPreferences,
        )
    }
   /* @Provides
    fun provideFilterRepositoryImpl(sharedPrefs: SharedPreferences): FilterRepositoryImpl {
        return FilterRepositoryImpl(
            sharedPrefs = sharedPrefs
        )
    }
    @Provides
    fun provideChooseCountryRepositoryImpl(
        apiService: ApiService,
        networkControl: ConnectivityHelper,
        converter: ModelConverter
    ): ChooseCountryRepositoryImpl {
        return ChooseCountryRepositoryImpl(
            apiService = apiService,
            networkControl = networkControl,
            converter = converter,
        )
    }
    @Provides
    fun provideChooseRegionRepositoryImpl(
        apiService: ApiService,
        networkControl: ConnectivityHelper,
        converter: ModelConverter
    ): ChooseRegionRepositoryImpl {
        return ChooseRegionRepositoryImpl(
            apiService = apiService,
            networkControl = networkControl,
            converter = converter,
        )
    }
    @Provides
    fun provideChooseIndustryRepositoryImpl(
        apiService: ApiService,
        networkControl: ConnectivityHelper,
        filterModelConverter: FilterModelConverter
    ): ChooseIndustryRepositoryImpl {
        return ChooseIndustryRepositoryImpl(
            apiService = apiService,
            networkControl = networkControl,
            filterModelConverter = filterModelConverter,
        )
    }
    @Provides
    fun provideVacancyDbRepositoryImpl(
        appDataBase: AppDB,
        vacancyDbConverter: ModelConverter
    ): VacancyDbRepositoryImpl {
        return VacancyDbRepositoryImpl(
            appDataBase = appDataBase,
            vacancyDbConverter = vacancyDbConverter,
        )
    }
    @Provides
    fun provideDetailVacancyRepositoryImpl(
        apiService: ApiService,
        networkControl: ConnectivityHelper,
        converter: ModelConverter,
        vacancyDb: VacancyDbRepository
    ): DetailVacancyRepositoryImpl {
        return DetailVacancyRepositoryImpl(
            apiService = apiService,
            networkControl = networkControl,
            converter = converter,
            vacancyDb = vacancyDb,
        )
    }
    @Provides
    fun provideSimilarVacancyRepositoryImpl(
        apiService: ApiService,
        networkControl: ConnectivityHelper,
        converter: ModelConverter
    ): SimilarVacancyRepositoryImpl {
        return SimilarVacancyRepositoryImpl(
            apiService = apiService,
            networkControl = networkControl,
            converter = converter,
        )
    }
    @Provides
    fun provideFavoriteRepositoryImpl(
        appDataBase: AppDB,
        converter: ModelConverter
    ): FavoriteRepositoryImpl {
        return FavoriteRepositoryImpl(
            converter = converter,
            appDataBase = appDataBase,
        )
    }*/
}

@Module
interface RepositoryBindModule{
    @Suppress("FunctionName")
    @Binds
    fun bindFilterRepositoryImpl_to_FilterRepository(
        filterRepositoryImpl: FilterRepositoryImpl
    ) : FilterRepository

    @Binds
    fun bindChooseCountryRepositoryImpl_to_ChooseCountryRepository(
        chooseCountryRepositoryImpl: ChooseCountryRepositoryImpl
    ) : ChooseCountryRepository
    @Binds
    fun bindChooseRegionRepositoryImpl_to_ChooseRegionRepository(
        chooseRegionRepositoryImpl: ChooseRegionRepositoryImpl
    ) : ChooseRegionRepository
    @Binds
    fun bindChooseIndustryRepositoryImpl_to_ChooseIndustryRepository(
        chooseIndustryRepositoryImpl: ChooseIndustryRepositoryImpl
    ) : ChooseIndustryRepository
    @Binds
    fun bindVacancyDbRepositoryImpl_to_VacancyDbRepository(
        vacancyDbRepositoryImpl: VacancyDbRepositoryImpl
    ) : VacancyDbRepository
    @Binds
    fun bindDetailVacancyRepositoryImpl_to_DetailVacancyRepository(
        detailVacancyRepositoryImpl: DetailVacancyRepositoryImpl
    ) : DetailVacancyRepository
    @Binds
    fun bindSimilarVacancyRepositoryImpl_to_SimilarVacancyRepository(
        similarVacancyRepositoryImpl: SimilarVacancyRepositoryImpl
    ) : SimilarVacancyRepository
    @Binds
    fun bindFavoriteRepositoryImpl_to_FavoriteRepository(
        favoriteRepositoryImpl: FavoriteRepositoryImpl
    ) : FavoriteVacancyRepository
}

/*val repositoryModule = module {

    factory<SearchRepository> {
        SearchRepositoryImpl(
            apiService = get(),
            networkControl = get(),
            converter = get(),
            sharedPreferences = get(),
        )
    }

    single<FilterRepository> {
        FilterRepositoryImpl(
            sharedPrefs = get()
        )
    }

    single<ChooseCountryRepository> {
        ChooseCountryRepositoryImpl(
            apiService = get(),
            networkControl = get(),
            converter = get()
        )
    }

    single<ChooseRegionRepository> {
        ChooseRegionRepositoryImpl(
            apiService = get(),
            networkControl = get(),
            converter = get()
        )
    }
    single<ChooseIndustryRepository> {
        ChooseIndustryRepositoryImpl(
            apiService = get(),
            networkControl = get(),
            filterModelConverter = get()
        )
    }

    single<VacancyDbRepository> {
        VacancyDbRepositoryImpl(
            appDataBase = get(),
            vacancyDbConverter = get()
        )
    }

    single<DetailVacancyRepository> {
        DetailVacancyRepositoryImpl(
            apiService = get(),
            networkControl = get(),
            converter = get(),
            vacancyDb = get()
        )
    }
    single<SimilarVacancyRepository> {
        SimilarVacancyRepositoryImpl(
            apiService = get(),
            networkControl = get(),
            converter = get(),
        )
    }

    single<FavoriteVacancyRepository> {
        FavoriteRepositoryImpl(
            converter = get(),
            appDataBase = get()
        )
    }
}*/

