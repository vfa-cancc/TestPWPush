# TestPWPush

#### Test channel: Create channel with name "Ch1" for your device token in installation class

#### Test Dialog type:
* In [Console pannel](https://console.mb.cloud.nifty.com/) check: Enable display Dialog Push
* Add User Setting Value:

{"type": 0}

OR {"type": 1}

OR {"type": 2}

OR {"type": 4}

with:
NCMBDialogPushConfiguration.DIALOG_DISPLAY_NONE = 0

NCMBDialogPushConfiguration.DIALOG_DISPLAY_DIALOG = 1

NCMBDialogPushConfiguration.DIALOG_DISPLAY_BACKGROUND = 2

NCMBDialogPushConfiguration.DIALOG_DISPLAY_ORIGINAL = 4

