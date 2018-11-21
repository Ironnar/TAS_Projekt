package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import project.model.Product;
import project.model.ProductUser;
import project.model.User;
import project.repositories.UserRepository;
import project.services.ProductService;
import project.services.ProductUserService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class ProductUserController {

    @Autowired
    private ProductUserService productUserService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;


    //Add Comment etc.
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @RequestMapping(value="/products/{id}",method=RequestMethod.POST)
    public ResponseEntity<String> createComment(@RequestBody @Nullable String comment,@RequestParam(value="rating",required = false) Integer rating,@PathVariable int id ){

        //Get current logged user
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser=userRepository.findOneByName(userDetails.getUsername());

        Product p = this.productService.getProductsById(id);

        if(currentUser==null){

            return new ResponseEntity<String>("Log in first to add a comment!", HttpStatus.BAD_REQUEST);
        }
        else{

            if(p==null){
                return new ResponseEntity<String>("Product does not exist!", HttpStatus.BAD_REQUEST);
            }else {

                if (this.productUserService.checkIfExists(id, currentUser.getId())) {
                    return new ResponseEntity<String>("Your comment already exists!", HttpStatus.BAD_REQUEST);
                } else {

                    ProductUser productUser = new ProductUser();
                    if(rating==null){
                        if(comment == null){
                            return new ResponseEntity<String>("Add a comment or rating first!", HttpStatus.BAD_REQUEST);
                        }
                        }
                    else {
                        if (rating.intValue() >= 0 && rating.intValue()<=6) {
                            productUser.setRating(rating.intValue());
                        }else{
                            return new ResponseEntity<String>("Rating has to be equal or greater than 0 and equal or greater than 6!", HttpStatus.BAD_REQUEST);
                        }
                    }
                    productUser.setComment(comment);
                    productUser.setProduct(p);
                    productUser.setUserP(currentUser);
                    this.productUserService.add(productUser);
                    return new ResponseEntity<String>("Comment added successfully!", HttpStatus.CREATED);
                }
            }
        }
    }

    //Delete Comment
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @RequestMapping(value = "/products/{id}/{commentId}",method=RequestMethod.DELETE)
    public ResponseEntity<String> deleteComment(@PathVariable int commentId,@PathVariable int id)
    {
        //Get current logged user
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser=userRepository.findOneByName(userDetails.getUsername());

        if(currentUser==null){

            return new ResponseEntity<String>("Log in first to delete a comment!", HttpStatus.BAD_REQUEST);
        }
        else {
            if (this.productService.getProductsById(id) == null) {
                return new ResponseEntity<String>("Product does not exist!", HttpStatus.BAD_REQUEST);
            } else {
                if (this.productUserService.checkIfExists(commentId)) {

                    ProductUser pu = this.productUserService.getProductUser(commentId);
                    int OwnerId = pu.getUserP().getId();

                    if (currentUser.getId() == OwnerId || currentUser.getRole().getName().contentEquals("ADMIN")) {
                        this.productUserService.delete(commentId);
                        return new ResponseEntity<String>("Comment deleted successfully!", HttpStatus.OK);
                    } else {
                        return new ResponseEntity<String>("You don't have permission to delete this comment!", HttpStatus.OK);
                    }
                } else {
                    return new ResponseEntity<String>("Comment does not exist!", HttpStatus.NOT_FOUND);
                }
            }
        }
    }
    //Edit
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @RequestMapping(value="/products/{id}/{commentId}",method=RequestMethod.PUT)
    public ResponseEntity<String> editComment(@RequestBody @Nullable String comment, @RequestParam(value="rating",required = false) Integer rating, @PathVariable int commentId,@PathVariable int id) {

        if(this.productService.getProductsById(id)!=null && this.productUserService.editComment(comment,rating,commentId)){
            return new ResponseEntity<String>("Edited successfully!",HttpStatus.OK);
        }
        else{
            return new ResponseEntity<String>("Comment does not exist or you don't have permission!",HttpStatus.BAD_REQUEST);
        }

    }

    //Get
    @RequestMapping(value="products/{id}/{commentId}",method = RequestMethod.GET)
    public ResponseEntity<ProductUser> getCommentsById(@PathVariable int id,@PathVariable int commentId){
        Product product = this.productService.getProductsById(id);
        ProductUser pu = this.productUserService.getProductUser(commentId);
        if(product!=null && pu!=null){
            return new ResponseEntity<ProductUser>(pu,HttpStatus.OK);
        }
        else{
            return new ResponseEntity<ProductUser>(pu,HttpStatus.BAD_REQUEST);
        }
    }
}
