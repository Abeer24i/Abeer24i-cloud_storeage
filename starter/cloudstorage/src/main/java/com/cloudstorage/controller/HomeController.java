package com.cloudstorage.controller;

import com.cloudstorage.model.File;
import com.cloudstorage.model.Note;
import com.cloudstorage.model.Credential;
import com.cloudstorage.model.User;
import com.cloudstorage.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;


@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private FileService fileService;

    @Autowired
    private NoteService noteService;

    @Autowired
    private CredentialService credentialService;

    @Autowired
    private UserService userService;

    @Autowired
    private EncryptionService encryptionService;

    private Note note;

    private String success = "redirect:/result?success=true";
    private String error = "redirect:/result?success=false";


    @GetMapping
    public String HomePage(
            Authentication authentication, @ModelAttribute("newFile") File file,
            @ModelAttribute("newNote") Note note, @ModelAttribute("newCredential") Credential credential,
            Model model) {
        Integer userId = getUserId(authentication);
        model.addAttribute("files", fileService.getFileListings(userId));
        model.addAttribute("notes", noteService.getNoteListings(userId));
        model.addAttribute("credentials", credentialService.getCredentialListings(userId));
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }

    private Integer getUserId(Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        return user.getUserid();
    }


    @PostMapping("/login")
    public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes redirectAttributes) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        redirectAttributes.addFlashAttribute("SuccessMessage", "Log out Successfully");
        return "redirect:/login";
    }


    @PostMapping("/file-upload")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile fileUpload, Model model, Authentication authentication) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        Integer userId = user.getUserid();

        if (fileUpload.isEmpty()) {
            return error;
        }

        try {
            fileService.saveFile(fileUpload,userName);
            model.addAttribute("files", fileService.getFileListings(userId));
            return success;
        } catch (Exception e) {
            return error;

        }
    }


    @GetMapping("/files/view/{id}")
    public void viewFile(@PathVariable Integer id, HttpServletResponse response) {
        File file = fileService.getAllFilebyid(id);
        if (file != null) {
            response.setContentType(file.getContenttype());
            response.setHeader("Content-Disposition", "attachment; filename=\"" + file.getFilename() + "\"");
            try {
                response.getOutputStream().write(file.getFiledata().readAllBytes());
                response.getOutputStream().close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    @GetMapping("/files/delete/{id}")
    public String deleteFile(@PathVariable Integer id) {
        fileService.deleteFile(id);
        try {
            return success;
        } catch (Exception e) {
            return error;
        }
    }


    @PostMapping("/addNote")
    public String addOrUpdateNote(Authentication authentication, @ModelAttribute Note newNote, Model model) {
        try {
            String userName = authentication.getName();
            String newTitle = newNote.getNotetitle();
            String newDescription = newNote.getNotedescription();

            if (newNote.getNoteId() == null) {
                noteService.addNote(newTitle, newDescription, userName);
            } else {
                noteService.updateNote(newNote.getNoteId(), newTitle, newDescription);
            }

            Integer userId = getUserId(authentication);
            model.addAttribute("notes", noteService.getNoteListings(userId));

            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return error;
        }
    }

    @GetMapping(value = "/get-note/{noteid}")
    public Note getNote(@PathVariable Integer noteId) {
        return noteService.getNoteById(noteId);
    }

    @PostMapping("/deleteNote")
    public String deleteNote(@RequestParam("noteid") Integer noteId) {
        noteService.deleteNote(noteId);
        try {
            return success;
        } catch (Exception e) {
            return error;
        }
    }

    @PostMapping("/addCredential")
    public String addOrUpdateCredential(
            Authentication authentication,
            @ModelAttribute("newCredential") Credential newCredential,
            Model model) {

        String user = authentication.getName();
        String newUrl = newCredential.getUrl();
        String password = newCredential.getPassword();

        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);

        if (newCredential.getCredentialId() == null) {
            credentialService.addCredential(newUrl, user, newCredential.getUsername(), encodedKey, encryptedPassword);
        } else {
            // Credential existingCredential = getCredential(Integer.parseInt(credentialIdStr));
            credentialService.updateCredential(newCredential.getCredentialId(), newCredential.getUsername(), newUrl, encodedKey, encryptedPassword);
        }
        User getuser = userService.getUser(user);
        model.addAttribute("credentials", credentialService.getCredentialListings(getuser.getUserid()));
        model.addAttribute("encryptionService", encryptionService);
        try {

            return success;
        } catch (Exception e) {
            return error;
        }
    }

    @GetMapping(value = "/get-credential/{credentialId}")
    public Credential getCredential(@PathVariable Integer credentialId) {
        return credentialService.getCredentialById(credentialId);
    }


    @PostMapping("/deleteCredential")
    public String deleteCredential(@RequestParam("credentialId") Integer credentialId) {
        credentialService.deleteCredential(credentialId);
        try {
            return success;
        } catch (Exception e) {
            return error;
        }
    }
}

