package com.amna.dailler

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.amna.dailler.databinding.FragmentContactDetailBinding
import com.amna.dailler.databinding.ItemContactInfoBinding
import com.amna.dailler.databinding.ViewActionButtonBinding
import kotlinx.coroutines.launch
import java.io.File

class ContactDetailFragment : Fragment() {

    private var _binding: FragmentContactDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DialerViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupRecentActivity()
        setupObservers()
    }

    private fun setupObservers() {
        val contactNumber = arguments?.getString("contactNumber") ?: ""
        
        // Observe all call logs and filter by this contact's number
        viewModel.callLogs.observe(viewLifecycleOwner) { allLogs ->
            val filteredLogs = allLogs.filter { it.number == contactNumber }
            (binding.rvRecent.adapter as? CallLogAdapter)?.submitList(filteredLogs)
        }
    }

    private fun setupUI() {
        binding.ivBack.setOnClickListener { findNavController().popBackStack() }
        
        val name = arguments?.getString("contactName") ?: "Unknown Contact"
        val number = arguments?.getString("contactNumber") ?: "No Number"
        val contactId = arguments?.getString("contactId") ?: "-1"
        
        binding.tvName.text = name

        // Fetch initial favorite status from contacts
        var isFavorite = false
        viewModel.contacts.observe(viewLifecycleOwner) { contacts ->
            val contact = contacts.find { it.id == contactId }
            isFavorite = contact?.isFavorite ?: false
            updateFavoriteIcon(isFavorite)
        }

        // Favorite Toggle
        binding.ivFav.setOnClickListener {
            viewLifecycleOwner.lifecycleScope.launch {
                val success = viewModel.toggleFavorite(contactId, isFavorite) 
                if (success) {
                    isFavorite = !isFavorite
                    updateFavoriteIcon(isFavorite)
                }
            }
        }
        
        // 3-dot menu
        binding.ivMore.setOnClickListener {
            showOptionsMenu(it, contactId, isFavorite)
        }
        
        // Setup Action Buttons
        val callBtn = ViewActionButtonBinding.bind(binding.btnCall.root)
        callBtn.ivIcon.setImageResource(R.drawable.ic_phone_24)
        callBtn.root.setOnClickListener {
            makeCall(number)
        }
        
        val msgBtn = ViewActionButtonBinding.bind(binding.btnMsg.root)
        msgBtn.ivIcon.setImageResource(R.drawable.ic_more_vert_24)
        msgBtn.root.setOnClickListener {
            sendMessage(number)
        }
        
        val videoBtn = ViewActionButtonBinding.bind(binding.btnVideo.root)
        videoBtn.ivIcon.setImageResource(R.drawable.ic_phone_24)
        videoBtn.root.setOnClickListener {
            makeVideoCall(number)
        }
        
        val emailBtn = ViewActionButtonBinding.bind(binding.btnMail.root)
        emailBtn.ivIcon.setImageResource(R.drawable.ic_more_vert_24)
        emailBtn.root.setOnClickListener {
            sendEmail()
        }

        // Setup Info Items
        ItemContactInfoBinding.bind(binding.infoPhone.root).apply {
            tvMain.text = number
            tvSub.text = "Mobile"
            ivIcon.setImageResource(R.drawable.ic_phone_24)
        }

        ItemContactInfoBinding.bind(binding.infoWhatsapp.root).apply {
            tvMain.text = "Chat with $number"
            tvSub.text = "WhatsApp"
            ivIcon.setImageResource(R.drawable.ic_contacts_24)
            root.setOnClickListener {
                openWhatsApp(number)
            }
        }

        ItemContactInfoBinding.bind(binding.infoGroups.root).apply {
            tvMain.text = "Standard"
            tvSub.text = "Contacts Group"
            ivIcon.setImageResource(R.drawable.ic_groups_24)
        }

        ItemContactInfoBinding.bind(binding.infoRingtone.root).apply {
            tvMain.text = "Default ringtone"
            tvSub.text = "Ringtone"
            ivIcon.setImageResource(R.drawable.ic_more_vert_24)
        }
    }
    
    private fun showOptionsMenu(view: View, contactId: String, isFavorite: Boolean) {
        val popup = androidx.appcompat.widget.PopupMenu(requireContext(), view)
        popup.menu.add("View call history")
        popup.menu.add("Block")
        popup.menu.add("Share")
        popup.menu.add(if (isFavorite) "Remove from favorites" else "Add to favorites")
        popup.menu.add("Merge/Split")
        popup.menu.add("Change account")
        popup.menu.add("Remove contact")
        popup.menu.add("Delete")
        popup.menu.add("Set ringtone")
        
        popup.setOnMenuItemClickListener { item ->
            when (item.title) {
                "View call history" -> {
                    findNavController().navigate(R.id.nav_history_detail, arguments)
                }
                "Block" -> {
                    android.widget.Toast.makeText(requireContext(), "Contact blocked", android.widget.Toast.LENGTH_SHORT).show()
                    // Note: Blocking requires system-level implementation
                }
                "Share" -> {
                    shareContact(contactId, arguments?.getString("contactName") ?: "Contact")
                }
                "Add to favorites", "Remove from favorites" -> {
                    binding.ivFav.performClick()
                }
                "Merge/Split" -> {
                    android.widget.Toast.makeText(requireContext(), "Merge/Split contact", android.widget.Toast.LENGTH_SHORT).show()
                }
                "Change account" -> {
                    android.widget.Toast.makeText(requireContext(), "Change account", android.widget.Toast.LENGTH_SHORT).show()
                }
                "Remove contact" -> {
                    android.widget.Toast.makeText(requireContext(), "Remove contact", android.widget.Toast.LENGTH_SHORT).show()
                }
                "Delete" -> {
                    showDeleteConfirmation(contactId)
                }
                "Set ringtone" -> {
                    android.widget.Toast.makeText(requireContext(), "Set ringtone", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
            true
        }
        popup.show()
    }
    
    private fun showDeleteConfirmation(contactId: String) {
        com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
            .setTitle("Delete contact")
            .setMessage("This action cannot be undone. Are you sure you want to delete this contact?")
            .setNegativeButton("No") { d, _ -> d.dismiss() }
            .setPositiveButton("Yes") { _, _ ->
                viewLifecycleOwner.lifecycleScope.launch {
                    val success = viewModel.deleteContact(contactId)
                    if (success) {
                        android.widget.Toast.makeText(requireContext(), "Contact deleted", android.widget.Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    } else {
                        android.widget.Toast.makeText(requireContext(), "Failed to delete contact", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .show()
    }

    private fun setupRecentActivity() {
        val adapter = CallLogAdapter(
            onItemClick = { /* No action */ },
            onInfoClick = { /* No action */ },
            onItemLongClick = { /* No action */ }
        )
        binding.rvRecent.adapter = adapter
        
        val logs = listOf(
            CallLogEntry("1", null, "0345 7455800", "32 min ago", CallType.INCOMING, "Pakistan"),
            CallLogEntry("2", null, "0324 7455870", "1 h ago", CallType.OUTGOING, "Pakistan"),
            CallLogEntry("3", null, "0324 7455870", "1 h ago", CallType.MISSED, "Pakistan")
        )
        adapter.submitList(logs)
    }

    private fun updateFavoriteIcon(isFavorite: Boolean) {
        // Use star icon for favorites
        binding.ivFav.setImageResource(R.drawable.ic_history)
        binding.ivFav.imageTintList = android.content.res.ColorStateList.valueOf(
            if (isFavorite) resources.getColor(R.color.primary_blue, null) 
            else resources.getColor(R.color.text_secondary_light, null)
        )
    }

    private fun makeCall(number: String) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CALL_PHONE) 
            == PackageManager.PERMISSION_GRANTED) {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$number")
            }
            startActivity(intent)
        } else {
            requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), 100)
        }
    }
    
    private fun sendMessage(number: String) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("smsto:$number")
        }
        startActivity(intent)
    }
    
    private fun makeVideoCall(number: String) {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$number")
            putExtra("android.phone.extra.VIDEO_CALLING", true)
        }
        try {
            startActivity(intent)
        } catch (e: Exception) {
            android.widget.Toast.makeText(requireContext(), "Video calling not supported", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun sendEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
        }
        startActivity(intent)
    }
    
    private fun openWhatsApp(number: String) {
        try {
            val cleanNumber = number.replace(Regex("[^0-9]"), "")
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://wa.me/$cleanNumber")
            }
            startActivity(intent)
        } catch (e: Exception) {
            android.widget.Toast.makeText(requireContext(), "WhatsApp not installed", android.widget.Toast.LENGTH_SHORT).show()
        }
    }
    
    private fun shareContact(contactId: String, contactName: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            val vCard = viewModel.getContactVCard(contactId)
            if (vCard != null) {
                try {
                    val file = File(requireContext().cacheDir, "$contactName.vcf")
                    file.writeText(vCard)
                    val uri = FileProvider.getUriForFile(
                        requireContext(),
                        "${requireContext().packageName}.fileprovider",
                        file
                    )
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/x-vcard"
                        putExtra(Intent.EXTRA_STREAM, uri)
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }
                    startActivity(Intent.createChooser(intent, "Share contact"))
                } catch (e: Exception) {
                    android.widget.Toast.makeText(requireContext(), "Failed to share contact", android.widget.Toast.LENGTH_SHORT).show()
                }
            } else {
                android.widget.Toast.makeText(requireContext(), "Failed to get contact data", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
