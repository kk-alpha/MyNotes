### TextInputLayout

```xml
<com.google.android.material.textfield.TextInputLayout
   android:layout_width="match_parent"
   android:layout_height="wrap_content"
   android:layout_margin="4dp"
   android:hint="@string/shr_hint_username">

   <com.google.android.material.textfield.TextInputEditText
       android:layout_width="match_parent"
       android:layout_height="wrap_content"
       android:inputType="text"
       android:maxLines="1" />
</com.google.android.material.textfield.TextInputLayout>
```



### MaterialButton

```xml
<com.google.android.material.button.MaterialButton
       android:id="@+id/next_button"
       android:layout_width="wrap_content"
       android:layout_height="wrap_content"
       android:layout_alignParentEnd="true"
       android:layout_alignParentRight="true"
       android:text="@string/shr_button_next" />
```

+ **Note**: The `MaterialButton` component has a default style of a colored, elevated button. For this Cancel button, however, we want an unelevated, unfilled button. To accomplish this, we apply a `style` attribute: `@style/Widget.MaterialComponents.Button.TextButton`.
+ 