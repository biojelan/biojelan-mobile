package id.biojelan.app.ui.strings

/**
 * English implementation of [BioStrings].
 */
object EnStrings : BioStrings {

    // ============================================================ Common
    override val appName = "BioJelan"
    override val reload = "Reload"
    override val back = "Back"
    override val save = "Save"
    override val cancel = "Cancel"
    override val accept = "Accept"
    override val close = "Close"
    override val total = "Total"
    override val or = "or"

    // ============================================================ Auth
    override val welcomeTitle = "Welcome to BioJelan"
    override val welcomeSubtitle = "The interface will adjust to your role"
    override val createAccountTitle = "Create a BioJelan account"
    override val createAccountSubtitle = "Your account is registered as a Client. To become an Agent, contact the Kilang team"
    override val signIn = "Sign In"
    override val signUp = "Sign Up"
    override val forgotPasswordLink = "Forgot password?"
    override val forgotPasswordTitle = "Forgot password"
    override val forgotPasswordHeadline = "We'll send a reset link to your email"
    override val forgotPasswordBody = "Enter your registered email. A link to create a new password will be sent there."
    override val sendResetLink = "Send reset link"
    override val resetSentNote = "If the email is registered, a reset link has been sent. Check your inbox (and spam folder)."
    override val guestEntry = "Browse as guest"
    override val retryButton = "Try again"

    override val fieldFullName = "Full name"
    override val fieldEmail = "Email"
    override val fieldPassword = "Password"
    override val fieldRepeatPassword = "Repeat password"
    override val placeholderName = "Your name"
    override val placeholderEmail = "name@email.com"
    override val placeholderPasswordMin = "At least 8 characters"
    override val placeholderPasswordYours = "Your password"
    override val placeholderRepeatPassword = "Re-type your password"

    // ============================================================ Navigation tabs
    override val tabHome = "Home"
    override val tabFindAgent = "Find Agent"
    override val tabTransactions = "Transactions"
    override val tabStock = "Stock"
    override val tabHistory = "History"
    override val tabProfile = "Profile"

    // ============================================================ Klien Home
    override val priceCaption = "Kilang reference price · applies to all Agents"
    override val quickActionFindAgent = "Find Agent"
    override val quickActionHistory = "History"
    override val quickActionMyId = "My ID"
    override val sectionAgenBioJelan = "BioJelan Agents"
    override val viewAll = "View all"
    override val noAgentsRegistered = "No agents registered yet."
    override val klienInfoNote = "Sell your used cooking oil to an Agent. The Agent records the transaction, then you confirm it here — payment is settled directly with the Agent."
    override val confirmTransaction = "Confirm transaction"
    override val open = "Open"
    override val closed = "Closed"
    override fun pendingTxBody(agenName: String, volume: String) =
        "$agenName recorded a sale of $volume cooking oil on your behalf."

    // ============================================================ Agen Home
    override val priceCaptionAgen = "Kilang reference price · used when recording transactions"
    override val todayTransactions = "Today's transactions"
    override val collectedToday = "Collected today"
    override val valueToday = "Value today"
    override val currentStockLabel = "CURRENT STOCK"
    override val readyForPickup = "Ready for Kilang pickup"
    override fun remainingToThreshold(remaining: String) = "$remaining more to threshold"
    override val newTransaction = "New Transaction"
    override val recentActivity = "Recent activity"
    override val seeAll = "All"
    override val noTransactionsYet = "No transactions yet."
    override val noTransactionsAgenHint = "No transactions yet. Record the first sale from a Client using the button above."

    // ============================================================ Agen Stock
    override val stockTitle = "Stock"
    override fun thresholdCaption(threshold: String) = "of $threshold threshold"
    override val aboveThresholdNote = "Above threshold — awaiting Kilang pickup schedule"
    override val stockThresholdInfo = "After stock exceeds the threshold, Kilang will schedule a pickup. The threshold is set by Kilang."
    override val stockMovement = "Stock movement"
    override val noStockMovement = "No movement yet. Stock increases when a Client accepts a transaction."
    override val stockCorrectionNote = "Stock discrepancy? Contact the Kilang team — stock correction is not yet available in the app."
    override val stockIncoming = "Incoming"
    override val stockWaiting = "Waiting"
    override val stockCancelled = "Cancelled"
    override fun transactionDash(name: String) = "Transaction — $name"

    // ============================================================ Agen Transactions
    override val transactionsTitle = "Transactions"
    override val transactionDetailTitle = "Transaction detail"
    override val todayLabel = "Today"
    override val volumeToday = "Volume today"
    override val noTransactionsTitle = "No transactions"
    override val noTransactionsHint = "Press the + button to record a sale from a Client."
    override val transactionButton = "Transaction"
    override val pendingAgenNote = "Waiting for the Client to accept or cancel in their app. Your stock increases after the Client accepts."
    override val cancelledAgenNote = "The Client cancelled this transaction. Your stock remains unchanged."

    override val labelTransactionId = "Transaction ID"
    override val labelDate = "Date"
    override val labelAgent = "Agent"
    override val labelClient = "Client"
    override val labelClientId = "Client ID"
    override val labelVolume = "Volume"
    override val labelPricePerLiter = "Price / liter"
    override val labelTotal = "Total"
    override val labelStatus = "Status"

    override val newTransactionTitle = "New Transaction"
    override val newTransactionNote = "Ask for the Client ID from the \"My ID\" menu in the Client's app. After submission, the Client will be asked to accept or cancel."
    override val fieldClientId = "Client ID"
    override val fieldClientName = "Client name"
    override val fieldVolume = "Oil volume (liters)"
    override val fieldPricePerLiter = "Price per liter"
    override val placeholderClientId = "ID from Client's app"
    override val placeholderClientName = "Client name"
    override val placeholderVolume = "e.g. 12.5"
    override val hintReferencePriceKilang = "Kilang reference price"
    override val submitToClient = "Send to Client"

    override val errorClientIdRequired = "Client ID is required."
    override val errorClientNameRequired = "Client name is required."
    override val errorVolumeRequired = "Enter a volume greater than 0."
    override val errorVolumeTooLarge = "Volume too large — please double-check."

    // ============================================================ Klien History
    override val historyTitle = "History"
    override val transactionsCount = "Transactions"
    override val oilSold = "Oil sold"
    override val totalReceived = "Total received"
    override val noHistoryTitle = "No transactions"
    override val noHistoryHint = "Transactions with Agents will appear here."
    override val pendingKlienNote = "Check the amount and price. Accept if it matches what you handed to the Agent; cancel if not."
    override val cancelNotUndoable = "Cancellation cannot be undone."
    override val yesCancel = "Yes, cancel"

    // ============================================================ Guest
    override val guestBannerTitle = "Browsing as guest."
    override val guestBannerBody = "Register to start selling cooking oil and view your transaction history. Tap to register."
    override val guestPriceCaption = "Today's cooking oil price · applies to all Agents"
    override val guestUpsellTitle = "Register to continue"
    override val guestUpsellBody = "As a guest, you can only view Agent locations on the map and current cooking oil prices. Register first to start selling cooking oil, view transaction history, and manage your profile."
    override val registerNow = "Register Now"
    override val maybeLater = "Maybe later"

    // ============================================================ Profile
    override val profileTitle = "Profile"
    override val roleAgent = "Agent"
    override val roleClient = "Client"
    override val sectionAccountData = "Account data"
    override val sectionAgentData = "Agent data"
    override val sectionSettings = "Settings"
    override val labelPhone = "Phone"
    override val labelIdAgent = "Agent ID"
    override val labelIdClient = "Client ID"
    override val labelAddress = "Address"
    override val labelOperatingHours = "Operating hours"
    override val labelBankAccount = "Bank account"
    override val labelStockThreshold = "Stock threshold"
    override val notFilled = "Not filled"
    override val setByKilang = "set by Kilang"
    override val editProfile = "Edit profile"
    override val changePassword = "Change password"
    override val logout = "Log out"
    override val deleteAccount = "Delete account"

    override val editProfileTitle = "Edit profile"
    override val fieldName = "Name"
    override val fieldPhone = "Phone number"
    override val fieldAddress = "Address"
    override val fieldBankName = "Bank name"
    override val fieldAccountNumber = "Account number"
    override val fieldOpenTime = "Open"
    override val fieldCloseTime = "Close"
    override val fieldOpenDays = "Open days"
    override val saveChanges = "Save changes"
    override val errorNameRequired = "Name is required."
    override val errorTimeFormat = "Format HH:mm"
    override val placeholderPhone = "08xxxxxxxxxx"
    override val placeholderAddress = "Full address"
    override val placeholderBankName = "e.g. BCA"
    override val placeholderOpenTime = "08:00"
    override val placeholderCloseTime = "17:00"

    override val changePasswordTitle = "Change password"
    override val fieldCurrentPassword = "Current password"
    override val fieldNewPassword = "New password"
    override val fieldRepeatNewPassword = "Repeat new password"
    override val hintMinChars = "At least 8 characters"
    override val errorCurrentPasswordRequired = "Current password is required."
    override val errorMinChars = "At least 8 characters."
    override val errorConfirmMismatch = "Confirmation does not match."
    override val savePassword = "Save password"

    override val deleteAccountTitle = "Delete account"
    override val deleteAccountWarning = "Your account and all data will be permanently deleted and cannot be recovered."
    override val deleteAccountConfirmLabel = "Type DELETE to continue"
    override val deleteAccountConfirmWord = "DELETE"
    override val deleteMyAccount = "Delete my account"

    // ============================================================ Transaction status labels
    override fun txStatusLabel(status: String, isKlien: Boolean): String = when (status.trim().lowercase()) {
        "pending" -> if (isKlien) "Awaiting you" else "Awaiting Client"
        "accepted" -> if (isKlien) "Completed" else "Accepted"
        "cancelled", "canceled" -> "Cancelled"
        else -> "—"
    }
    override val statusCancelled = "Cancelled"

    // ============================================================ Greeting
    override fun greeting(hour: Int): String = when {
        hour < 11 -> "Good morning"
        hour < 15 -> "Good afternoon"
        hour < 18 -> "Good evening"
        else -> "Good night"
    }

    // ============================================================ Date/Time
    override fun relativeToday(time: String) = "Today, $time"
    override fun relativeYesterday(time: String) = "Yesterday, $time"
    override val everyDay = "Every day"
    override val hoursNotSet = "Hours not set"
    override val timezone = "WIB"
    override val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    // ============================================================ Errors
    override val networkError = "Unable to connect to server. Check your internet connection."
    override val sessionExpired = "Your session has expired. Please sign in again."
    override fun serverError(code: Int) = "A server error occurred ($code)."
    override val parseError = "Server response format is invalid."

    // ============================================================ ID overlay
    override val myIdTitle = "My ID"
    override val defaultNickname = "BioJelan User"
}
