package com.amarasiricoreservice.service;

import com.amarasiricoreservice.Repository.LeaseDocumentRepository;
import com.amarasiricoreservice.entity.LeaseDocuments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LeaseDocumentService {

    @Autowired
    LeaseDocumentRepository leaseDocumentRepository;

    public List<LeaseDocuments> saveLeaseDocuments(List<LeaseDocuments> leaseDocuments){

        return leaseDocumentRepository.saveAll(leaseDocuments);
    }

    public List<String> listFiles(Integer leaseId) {
       List<LeaseDocuments> leaseDocuments=   this.leaseDocumentRepository.findByLeaseKey(leaseId);
        List<String> files = leaseDocuments.stream().map(LeaseDocuments::getDocumentPath).collect(Collectors.toList());

    return files;
    } public List<LeaseDocuments> getDocs(Integer leaseId) {
       List<LeaseDocuments> leaseDocuments=   this.leaseDocumentRepository.findByLeaseKey(leaseId);
//        List<String> files = leaseDocuments.stream().map(LeaseDocuments::getDocumentPath).collect(Collectors.toList());

    return leaseDocuments;
    }

}
