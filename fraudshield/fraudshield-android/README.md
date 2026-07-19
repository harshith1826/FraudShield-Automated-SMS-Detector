# FraudShield — Android App (Shreya's part)

## What's already built
- `SmsReceiver` — wakes up on incoming SMS (Step 3)
- `UrlExtractor` — pulls URL out of SMS body, all formats (Step 4)
- `db/` — Room database, 4 tables, seeded with starter patterns (Step 5)
- `OnDeviceChecker` — Checks A/B/C, runs in <50ms (Step 6)
- `network/` — Retrofit models + API matching the LOCKED contract (Step 7)
- `SmsProcessor` — the pipeline that ties it all together, drives the
  grey → yellow → red/green notification states (Step 8)
- `ui/NotificationHelper` + `FeedbackActionReceiver` — verdict notifications
  with "Yes Fraud" / "This is Safe" buttons (Step 9)
- `workers/SyncWorker` — 15-min background sync, polling, delta-sync (Step 10)
- `MainActivity` — permission requests + schedules the sync worker
- `FraudShieldMessagingService` — FCM retroactive push (Week 3 item, stubbed)

## How to open and run this TODAY

### 1. Open in Android Studio
- Open Android Studio → "Open" → select the `fraudshield-android` folder.
- Let Gradle sync. If it complains about missing Kotlin/AGP versions, click
  "Upgrade" when prompted — versions here are recent but Studio may suggest newer patch versions.

### 2. Set the backend URL (BLOCKING — do this first)
Open `app/build.gradle`, find this line:
```groovy
buildConfigField "String", "BASE_URL", '"https://YOUR-BACKEND-URL-HERE/"'
```
Replace with Harshit's actual URL (his local IP, ngrok URL, or deployed URL).
**Ask Harshit for this right now if you don't have it.**
Must end with a trailing `/`. Then Gradle Sync again.

### 3. (Optional today) Firebase setup
If you want FCM retroactive push working, you need `google-services.json`
from the Firebase console, placed in `app/`. **This is a Week 3 item — skip
it today** unless you have spare time; the app builds and runs fine without
it as long as you also temporarily remove the `google-services` plugin line
in `app/build.gradle` and the FCM dependency, OR just drop a placeholder
`google-services.json` (ask teammates if anyone already created the Firebase
project — don't create a second one).

### 4. Run on a real device (NOT emulator)
SMS interception needs a real Android phone with a SIM, or you fake an SMS:
- **Real device**: Settings → Developer Options → enable USB debugging →
  connect via USB → select your device in Android Studio's device dropdown → Run ▶
- **Emulator workaround**: emulators can't receive real SMS, but you can fake one via:
  ```
  adb emu sms send <sender_number> "Your KYC expires. Update: sbi-kyc.xyz"
  ```
  Run this from a terminal while the emulator is running and your app is installed.

### 5. Grant permissions
On first launch, the app asks for SMS + notification permissions. Grant both
or nothing will work.

### 6. Test the 3 demo scenarios
Send/fake these SMS to trigger each flow:
- **Red (fraud)**: `Your KYC expires today. Update: sbi-kyc.xyz` from sender `VM-SBIBNK`
- **Green (safe)**: a message with a URL matching a domain you've added to
  the `url_cache` whitelist (or matching the seeded sender allowlist domain
  e.g. `sbi.co.in`)
- **Yellow (suspicious)**: any link with a `.xyz`/`.top` TLD or a `kyc`/`otp`
  keyword in the domain, sent from an unregistered sender

You should see a notification go grey → yellow → red/green within ~1 second
(or stay on the on-device verdict if the backend call fails — check Logcat
filtered by `okhttp` to see if `/api/ingest` actually reached Harshit's server).

## Today's checklist with teammates
- [ ] Got Harshit's live base URL, pasted into `build.gradle`
- [ ] Confirmed `/api/ingest` JSON field names match exactly (sender_id, sms_body, extracted_url, timestamp, device_id)
- [ ] Confirmed `/api/getmlfeedback/{job_id}` response field names match (especially `warning_text_*`, `action`)
- [ ] Tested one real end-to-end call: fake SMS → notification updates with Harshit's real (or mock) verdict

## If something breaks
- **App crashes on SMS receive**: check Logcat for the exact exception —
  most likely a null `BuildConfig.BASE_URL` (Gradle didn't sync) or a Room
  migration issue (delete app data and reinstall, since `fallbackToDestructiveMigration()`
  isn't set — add it to `AppDatabase.kt` if you change table schemas mid-day).
- **Notification never updates past yellow/grey**: backend unreachable —
  check the URL in `build.gradle`, check Harshit's server is actually running,
  check your phone/emulator and his server are on the same network (if local).
- **No notification at all**: permissions not granted, or SMS didn't contain
  anything matching the URL regex — test with the exact sample SMS above first.
