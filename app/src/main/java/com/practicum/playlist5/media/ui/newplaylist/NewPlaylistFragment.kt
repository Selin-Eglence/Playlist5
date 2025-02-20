package com.practicum.playlist5.media.ui.newplaylist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.practicum.playlist5.R
import com.practicum.playlist5.databinding.FragmentNewPlaylistBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import com.google.android.material.dialog.MaterialAlertDialogBuilder as DialogMaterialAlertDialogBuilder

class NewPlaylistFragment: Fragment() {
    private var _binding: FragmentNewPlaylistBinding?= null
    private val binding get() = _binding!!

    private var image: Uri? = null

    private lateinit var confirmDialog:AlertDialog


    private val newPlaylistViewModel: NewPlaylistViewModel by viewModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewPlaylistBinding.inflate(inflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonCreate.isEnabled=false
        binding.newPlaylist.setNavigationOnClickListener{
            if ( image != null || binding.nameInput.toString().isNotEmpty() || binding.descriptionInput.toString().isNotEmpty()) {
                confirmDialog.show()
            }
            else{
                findNavController().navigateUp()
            }
        }


       val textWatcher = object : TextWatcher {
           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
           override fun afterTextChanged(s: Editable?) {
               binding.buttonCreate.isEnabled = s?.isNotEmpty() ?: false
           }
       }

        binding.nameInputText.addTextChangedListener(textWatcher)



        binding.buttonCreate.setOnClickListener{
            Log.e("pressed", "создание плейлиста")
            val name = binding.nameInputText.text.toString()
            newPlaylistViewModel.setPlaylistName(name)
            val description = binding.descriptionInputText.text.toString()
            newPlaylistViewModel.setPlaylistDescription(description)
            val cover = image
            if (cover != null) {
                val privateStorageUri = saveImageToPrivateStorage(cover)
                newPlaylistViewModel.setCoverImageUri(privateStorageUri)
            }
            newPlaylistViewModel.savePlaylist()
        }



        newPlaylistViewModel.savePlaylistResult.observe(viewLifecycleOwner) { result ->
            result.fold(
                onSuccess = {
                    val playlistName = newPlaylistViewModel.getCurrentPlaylistName()
                    Toast.makeText(requireContext(), "Плейлист '$playlistName' создан!", Toast.LENGTH_SHORT).show()
                    findNavController().navigateUp()
                },
                onFailure = { exception ->
                    val errorMessage = exception.message ?: "Неизвестная ошибка"
                    Toast.makeText(requireContext(), "Ошибка: $errorMessage", Toast.LENGTH_LONG).show()
                }
            )
        }



        val pickImageLauncher = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { cover ->
                if (cover!= null) {
                    image = cover
                    convertImageIntoView(cover)
                } else {
                    Toast.makeText(requireContext(), "Изображение не выбрано", Toast.LENGTH_SHORT)
                        .show()
                }
            }

        binding.playlistImage.setOnClickListener {
            pickImageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (
                        image != null || binding.nameInputText.toString().isNotEmpty()
                        || binding.descriptionInputText.toString().isNotEmpty()
                    ) {
                        confirmDialog.show()
                    } else {
                        findNavController().navigateUp()
                    }
                }
            })

        confirmDialog = DialogMaterialAlertDialogBuilder(requireActivity())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setNegativeButton("Отмена") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("Завершить") { dialog, which ->
                requireActivity().supportFragmentManager.popBackStack()
            }
            .create()







    }
    private fun convertImageIntoView(image: Uri) {
        Glide.with(requireContext())
            .load(image)
            .apply(RequestOptions().transform(MultiTransformation(CenterCrop(),RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.radius_8dp)))))
            .placeholder(R.drawable.placeholder_image)
            .into(binding.playlistImage)
    }


    private fun saveImageToPrivateStorage(uri: Uri): Uri {
        val filePath = File(
            requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "playlistsImages"
        )

        if (!filePath.exists()) {
            filePath.mkdirs()
        }

        val file = File(filePath, uri.toString().substringAfterLast("/"))

        val inputStream = requireActivity().contentResolver.openInputStream(uri)

        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        return file.toUri()
    }






}