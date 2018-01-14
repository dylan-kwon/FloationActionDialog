FloatingActionDialog
====================

Preview
-------

<p>
  <img src="http://drive.google.com/uc?export=view&id=1aI1XZdToxPe-PQYHRhPnz2qfNbmdFeVr" width="250" height="435">
</p>

<br/>

How to use
----------

**1. Make a menu xml**

```xml
  menu_floating_dialog_item.xml

  <?xml version="1.0" encoding="utf-8"?>
  <menu xmlns:android="http://schemas.android.com/apk/res/android">

      <item
          android:id="@+id/menu_floating_dialog_item_one"
          android:icon="@drawable/ic_add_white_24dp"
          android:orderInCategory="0"
          android:title="@string/item1" />

      <item
          android:id="@+id/menu_floating_dialog_item_two"
          android:icon="@drawable/ic_add_white_24dp"
          android:orderInCategory="1"
          android:title="@string/item2" />

      <item
          android:id="@+id/menu_floating_dialog_item_three"
          android:icon="@drawable/ic_add_white_24dp"
          android:orderInCategory="2"
          android:title="@string/item3" />

      <item
          android:id="@+id/menu_floating_dialog_item_fore"
          android:icon="@drawable/ic_add_white_24dp"
          android:orderInCategory="3"
          android:title="@string/item4" />

      <item
          android:id="@+id/menu_floating_dialog_item_five"
          android:icon="@drawable/ic_add_white_24dp"
          android:orderInCategory="4"
          android:title="@string/item5" />
```

<br/>

**2. Show FloatingActionDialog**

```java
private void showFloatingActionDialog() {
        Fragment dialog = getSupportFragmentManager().findFragmentByTag(DIALOG_TAG_FLOATING_ACTION_DIALOG);

        if (dialog != null && dialog.isAdded()) {
            return;
        }

        final FloatingActionDialog floatingActionDialog = new FloatingActionDialog.Builder(anchorView)
                .setMenu(R.menu.menu_floating_dialog_item)
                .setOnItemClickListener(menuItem -> Toast.makeText(mContext, menuItem.getTitle(), Toast.LENGTH_SHORT).show())
                .build();

        floatingActionDialog.show(getSupportFragmentManager(), DIALOG_TAG_FLOATING_ACTION_DIALOG);
    }
```

<br/>

**3. Customize Function**

```java
.setMenu(int menuId);
.setItemBackgroundColor(int resId);
.setCloserBackgroundColor(int resId);
.setLabelAnimationDuration(int duration);
.setShowAnimationDuration(int duration);
.setDismissAnimationDuration(int duration);
.setCancelable(boolean cancelable);
.setDimAmount(float dimAmount);
.setOnDismissListener(OnDismissListener listener);
.setOnItemClickListener(OnItemClickListener listener);
```
