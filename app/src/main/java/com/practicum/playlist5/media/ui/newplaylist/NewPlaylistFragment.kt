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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.navigateUp
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.practicum.playlist5.R
import com.practicum.playlist5.databinding.FragmentNewPlaylistBinding
import com.practicum.playlist5.media.ui.playlist.Playlist
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream
import com.google.android.material.dialog.MaterialAlertDialogBuilder as DialogMaterialAlertDialogBuilder

class NewPlaylistFragment: Fragment() {
    private var _binding: FragmentNewPlaylistBinding?= null
    private val binding get() = _binding!!

    private var image: Uri? = null

    private lateinit var confirmDialog:AlertDialog

    private val args by navArgs<NewPlaylistFragmentArgs>()


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


        val editablePlaylist = args.playlist
        if (editablePlaylist == null) {
            binding.newPlaylist.title = getString(R.string.new_playlist)
            binding.buttonCreate.text = getString(R.string.create)
            binding.buttonCreate.isEnabled = false
        } else {
            binding.newPlaylist.title = getString(R.string.edit_playlist)
            binding.buttonCreate.text = getString(R.string.save)
            binding.nameInputText.setText(editablePlaylist.name)
            binding.descriptionInputText.setText(editablePlaylist.description)
            binding.buttonCreate.isEnabled = true

            editablePlaylist.imagePath?.let { uri ->
                convertImageIntoView(uri.toUri())
                image = uri.toUri()
            }
        }

        newPlaylistViewModel.playlistName.observe(viewLifecycleOwner) { name ->
            if (binding.nameInputText.text.toString() != name) {
                binding.nameInputText.setText(name)
        }}

        newPlaylistViewModel.playlistDescription.observe(viewLifecycleOwner) { description ->
            if (binding.descriptionInputText.text.toString() != description) {
                binding.descriptionInputText.setText(description)
        }}


        binding.newPlaylist.setNavigationOnClickListener{
           if(editablePlaylist!=null) {
               findNavController().navigateUp()
           }
               else {
                   if ( image != null || binding.nameInput.toString().isNotEmpty() || binding.descriptionInput.toString().isNotEmpty()){
                       showConfirmationDialogPlaylist()}
                   else{
                           findNavController().navigateUp()
                   }
               }
           }


       val textWatcher = object : TextWatcher {
           override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
           override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
           override fun afterTextChanged(s: Editable?) {
               binding.buttonCreate.isEnabled = s?.isNotEmpty() ?: false
               newPlaylistViewModel.setPlaylistName(s.toString())
           }
       }

        binding.nameInputText.addTextChangedListener(textWatcher)



        binding.buttonCreate.setOnClickListener{
            Log.e("pressed", "создание плейлиста")
            val name = binding.nameInputText.text.toString()
            newPlaylistViewModel.setPlaylistName(name)
            val description = binding.descriptionInputText.text.toString()
            newPlaylistViewModel.setPlaylistDescription(description)
            lifecycleScope.launch {
                image?.let { uri ->
                    if (uri.toString() != args.playlist?.imagePath.toString()) {
                        val privateStorageUri = saveImageToPrivateStorage(uri)
                        image = privateStorageUri
                        newPlaylistViewModel.setCoverImageUri(privateStorageUri)
                    }
                }}
            if (editablePlaylist == null) {
                saveNewPlaylist(name, description)
            } else {
                editExistingPlaylist(editablePlaylist, name, description)
            }


            findNavController().navigateUp()
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
                    if(editablePlaylist!=null) {
                        findNavController().navigateUp()
                    }
                    else {
                        if ( image != null || binding.nameInput.toString().isNotEmpty() || binding.descriptionInput.toString().isNotEmpty()){

                            showConfirmationDialogPlaylist()}
                        else{
                            findNavController().navigateUp()
                        }
                    }
                }
            })









    }

    private fun showConfirmationDialogPlaylist() {
        DialogMaterialAlertDialogBuilder(requireContext())
            .setTitle("Завершить создание плейлиста?")
            .setMessage("Все несохраненные данные будут потеряны")
            .setPositiveButton("Завершить") { dialog, _ ->
                findNavController().navigateUp()
                dialog.dismiss()
            }
            .setNegativeButton("Отмена") {dialog, which ->
                dialog.dismiss()
            }
            .show()
    }

    private fun saveNewPlaylist(name: String, description: String) {
        val newPlaylist = Playlist(
            0,
            name,
            description,
            image?.toString(),
            emptyList(),
            0
        )

        newPlaylistViewModel.savePlaylist(newPlaylist)
    }

    private fun editExistingPlaylist(playlist: Playlist, name: String, description: String) {
        val updatedPlaylist =
            playlist.copy(name = name, description = description, imagePath =image?.toString())

        newPlaylistViewModel.saveEditPlaylist(updatedPlaylist)
    }


    private fun convertImageIntoView(image: Uri) {
        Glide.with(requireContext())
            .load(image)
            .apply(RequestOptions().transform(MultiTransformation(CenterCrop(),RoundedCorners(requireContext().resources.getDimensionPixelSize(R.dimen.radius_8dp)))))
            .placeholder(R.drawable.placeholder_image)
            .into(binding.playlistImage)
    }


    private suspend fun saveImageToPrivateStorage(uri: Uri): Uri {
        return withContext(Dispatchers.IO) {
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

         file.toUri()
    }}






}