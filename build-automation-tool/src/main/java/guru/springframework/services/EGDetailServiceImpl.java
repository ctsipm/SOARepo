package guru.springframework.services;


import guru.springframework.domain.EGDetail;
import guru.springframework.repositories.EGDetailRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EGDetailServiceImpl implements EGDetailService {
    private EGDetailRepository egdetailRepository;

    @Autowired
    public void setEGDetailRepository(EGDetailRepository egdetailRepository) {
        this.egdetailRepository = egdetailRepository;
    }

    @Override
    public Iterable<EGDetail> listAllEGDetails() {
    	
    	return egdetailRepository.findAll();
    }

    @Override
    public EGDetail getEGDetailById(Integer id) {
        return egdetailRepository.findOne(id);
    }

    @Override
    public EGDetail saveEGDetail(EGDetail egdetail) {
        return egdetailRepository.save(egdetail);
    }

    @Override
    public void deleteEGDetail(Integer id) {
        egdetailRepository.delete(id);
    }
}
