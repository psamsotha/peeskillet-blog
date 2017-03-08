package io.github.psamsotha.blog.posts.controller;

import io.github.psamsotha.blog.posts.domain.Tag;
import io.github.psamsotha.blog.posts.domain.TagResource;
import io.github.psamsotha.blog.posts.domain.TagResourceAssembler;
import io.github.psamsotha.blog.posts.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * @author Paul Samsotha.
 */
@RestController
@RequestMapping(value = "/api/tags", produces = MediaType.APPLICATION_JSON_VALUE)
public class TagController {

    private final TagService tagService;
    private final TagResourceAssembler tagAssembler;


    @Autowired
    public TagController(TagService tagService, TagResourceAssembler tagAssembler) {
        this.tagService = tagService;
        this.tagAssembler = tagAssembler;
    }


    @GetMapping
    public ResponseEntity<PagedResources<TagResource>> getTags(Pageable pageable,
                                                               PagedResourcesAssembler assembler) {
        Page<Tag> page = this.tagService.getTags(pageable);
        PagedResources<TagResource> resource = assembler.toResource(page, this.tagAssembler);
        return ResponseEntity.ok(resource);
    }


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TagResource> createTag(Tag tag) {
        Tag created = this.tagService.createTag(tag);
        TagResource resource = this.tagAssembler.toResource(created);
        return ResponseEntity.status(HttpStatus.CREATED)
                .location(URI.create(resource.getId().getHref()))
                .body(resource);
    }
}