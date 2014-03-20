package org.zols.datastore.web.controller;

import com.zols.languagemanager.DocumentStorageManager;
import com.zols.languagemanager.domain.DocumentStorage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.zols.datastore.domain.Document;

@Controller
public class DocumentController {

    @Autowired
    DocumentStorageManager documentStorageManager;

    @RequestMapping(value = "/documents/{name}", method = RequestMethod.GET)
    public String documents(@PathVariable(value = "name") String name, Model map) {
        map.addAttribute("documentStorage", documentStorageManager.get(name));
        return "com/zols/datastore/document";
    }

    @RequestMapping(value = "/documents/{name}", method = RequestMethod.POST)
    public String save(@PathVariable(value = "name") String name, @ModelAttribute("document") Document document,Model map) throws IOException {
        DocumentStorage documentStorage = documentStorageManager.get(name);
        map.addAttribute("documentStorage", documentStorage);
        List<MultipartFile> files = document.getFiles();
        String fileName = null ;
        if (null != files && files.size() > 0) {
            for (MultipartFile multipartFile : files) {
                //Handle file content - multipartFile.getInputStream()
                fileName = multipartFile.getOriginalFilename();                  
                byte[] bytes = multipartFile.getBytes();
                BufferedOutputStream stream
                        = new BufferedOutputStream(new FileOutputStream(new File(documentStorage.getPath() + File.separator + multipartFile.getOriginalFilename())));
                stream.write(bytes);
                stream.close();
                

            }
        }

        return "com/zols/datastore/document";
    }
}
