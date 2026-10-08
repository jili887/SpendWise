# SpendWise 💰

A modern Android personal finance app for tracking income and expenses.

SpendWise was built with Kotlin and Jetpack Compose to demonstrate modern Android development practices, including MVVM architecture, dependency injection, local persistence, reactive UI state, navigation, input validation, and automated testing.

## Features
- Add income and expense transactions
- Edit and delete transactions
- Real-time balance, income, and expense calculations
- Transaction history
- Date picker
- Input validation
- Loading, empty, and error states
- Persistent local storage
- Reactive UI updates

## Highlights
- Modern Android architecture using MVVM
- Fully Compose-based UI with Material 3
- Room persistence with reactive database updates
- Hilt dependency injection
- StateFlow + Coroutines for state management
- Add/Edit/Delete transaction flows
- Input validation and error handling
- Unit-tested ViewModels and business logic


## Tech Stack
- Kotlin
- Jetpack Compose
- Material 3
- MVVM
- Hilt
- Room
- Kotlin Coroutines
- StateFlow
- Navigation Compose
- JUnit
- MockK
- Kotlin Coroutines Test

## Architecture
```text
Jetpack Compose UI
        ↓
    ViewModel
        ↓
   Repository
        ↓
   Room Database
```

The UI is built with Jetpack Compose and observes state exposed by ViewModels using StateFlow.

Hilt is used for dependency injection, while Room provides local persistence. Repository classes separate data access from UI and ViewModel logic.

### Key Android Skills Demonstrated
- Modern Kotlin development
- Jetpack Compose
- MVVM architecture
- Dependency injection with Hilt
- Room database and reactive data
- StateFlow and Coroutines
- Navigation
- Form validation
- Error and loading state handling
- Unit testing

### Screenshots
#### 1. Home
![SpendWise_HomeScreen.png](../../Personal/%E5%89%AF%E4%B8%9A/Side_Project/SpendWise_HomeScreen.png)
#### 2. Add Transaction
![SpendWise_AddTransaction.png](../../Personal/%E5%89%AF%E4%B8%9A/Side_Project/SpendWise_AddTransaction.png)
#### 3. Edit Transaction
![SpendWise_EditTransaction.png](../../Personal/%E5%89%AF%E4%B8%9A/Side_Project/SpendWise_EditTransaction.png)
#### 4. Statistics 
![SpendWise_Statistics.png](../../Personal/%E5%89%AF%E4%B8%9A/Side_Project/SpendWise_Statistics.png)
#### 5. Empty state
![SpendWise_EmptyState.png](../../Personal/%E5%89%AF%E4%B8%9A/Side_Project/SpendWise_EmptyState.png)
### Testing

Unit tests cover key application behavior including:
- Input validation
- ViewModel state changes
- Add and edit transactions
- Repository errors
- Date conversion
- Invalid input handling

