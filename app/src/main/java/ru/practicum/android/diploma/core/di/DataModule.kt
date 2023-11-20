package ru.practicum.android.diploma.core.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.practicum.android.diploma.core.application.App.Companion.SHARED_PREFS
import ru.practicum.android.diploma.db.data.dao.VacancyDao
import ru.practicum.android.diploma.db.domain.AppDB
import ru.practicum.android.diploma.filters.data.converter.FilterModelConverter
import ru.practicum.android.diploma.search.data.network.ApiService
import ru.practicum.android.diploma.search.data.network.ConnectivityHelper
import ru.practicum.android.diploma.search.data.network.ModelConverter

@Module
class DataModule {
    @Provides
    fun provideRetrofitBuilder(): ApiService {
        return Retrofit.Builder()
            .baseUrl("https://api.hh.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
    @Provides
    fun provideDatabaseBuilder(context: Context): AppDB {
        return Room.databaseBuilder(context, AppDB::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    fun provideVacancyDao(appDB: AppDB): VacancyDao {
        return appDB.vacancyDao()
    }
    @Provides
    fun provideSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFS, Application.MODE_PRIVATE)
    }
    @Provides
    fun provideConnectivityHelper(context: Context): ConnectivityHelper {
        return ConnectivityHelper(
            context = context
        )
    }
    @Provides
    fun provideModelConverter(context: Context): ModelConverter {
        return ModelConverter(
            context = context
        )
    }
    @Provides
    fun provideFilterModelConverter(): FilterModelConverter {
        return FilterModelConverter()
    }
}
/*val dataModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://api.hh.ru")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    single {
        Room.databaseBuilder(androidContext(), AppDB::class.java, "database.db")
            .fallbackToDestructiveMigration()
            .build()
    }

    single<VacancyDao> { get<AppDB>().vacancyDao() }

    single<SharedPreferences> {
        androidContext().getSharedPreferences(SHARED_PREFS, Application.MODE_PRIVATE)
    }

    single {
        ConnectivityHelper(
            context = get()
        )
    }

    single {
        ModelConverter(
            context = get()
        )
    }

    single {
        FilterModelConverter()
    }


}*/