# TestPWPush

#### Normal push (Title, Message)

#### Push with json data (Title, Message, User Setting Value (JSON))

#### Rich Push (Title, Message, URL)

#### Test channel: Create channel with name "Ch1" for your device token in installation class

#### Dialog Push (4 types):
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

#### Push with Delivery-Time (Title, Message, Delivery Time)

