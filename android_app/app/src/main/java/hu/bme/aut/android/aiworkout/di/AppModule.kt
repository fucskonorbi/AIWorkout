package hu.bme.aut.android.aiworkout.di

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hu.bme.aut.android.aiworkout.data.AuthRepositoryImpl
import hu.bme.aut.android.aiworkout.data.UserWorkoutsRepositoryImpl
import hu.bme.aut.android.aiworkout.domain.*
import hu.bme.aut.android.aiworkout.util.YuvToRgbConverter
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Provides
    @Singleton
    fun provideUserWorkoutsRepository(
        workoutsReference: CollectionReference
    ): UserWorkoutsRepository {
        return UserWorkoutsRepositoryImpl(workoutsReference)
    }

    @Provides
    @Singleton
    fun provideFirebaseCollectionReference(
        firebaseFirestore: FirebaseFirestore
    ): CollectionReference {
        return firebaseFirestore.collection("workouts")
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideMoveNetDetector(@ApplicationContext appContext: Context): MoveNet {
        return MoveNet.create(appContext)
    }

    @Provides
    @Singleton
    fun providePoseClassifier(@ApplicationContext appContext: Context): PoseClassifier {
        return PoseClassifier.create(appContext)
    }

    @Provides
    @Singleton
    fun provideYuvToRgbConverter(@ApplicationContext appContext: Context): YuvToRgbConverter {
        return YuvToRgbConverter(appContext)
    }
}