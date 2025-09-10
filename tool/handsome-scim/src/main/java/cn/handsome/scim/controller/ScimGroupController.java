package cn.handsome.scim.controller;

import cn.handsome.scim.model.ScimError;
import cn.handsome.scim.model.ScimGroup;
import cn.handsome.scim.model.ScimListResponse;
import cn.handsome.scim.model.ScimPatch;
import cn.handsome.scim.service.ScimGroupService;
import cn.handsome.web.base.BaseController;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author luoyong
 * @date 2025/9/10
 */
@RestController
@RequestMapping("/scim/v2/Groups")
@Api(value = "group", tags = "Group 服务")
public class ScimGroupController extends BaseController {

    private final ScimGroupService groupService;

    @Autowired
    public ScimGroupController(ScimGroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping
    public ResponseEntity<?> createGroup(@RequestBody ScimGroup group) {
        try {
            ScimGroup createdGroup = groupService.createGroup(group);
            return new ResponseEntity<>(createdGroup, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new ScimError(400, "invalidValue", e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroup(@PathVariable String id) {
        try {
            return ResponseEntity.ok(groupService.getGroupById(id));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new ScimError(404, "notFound", e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping
    public ResponseEntity<?> getGroups(
            @RequestParam(defaultValue = "1") int startIndex,
            @RequestParam(defaultValue = "100") int count,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String attributes
    ) {
        return ResponseEntity.ok(groupService.getGroups(filter, attributes, startIndex, count));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroup(@PathVariable String id, @RequestBody ScimGroup group) {
        try {
            return ResponseEntity.ok(groupService.updateGroup(id, group));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new ScimError(404, "notFound", e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScimGroup> patchGroup(@PathVariable String id, @RequestBody ScimPatch patch) {
        return ResponseEntity.ok(groupService.patchGroup(id, patch));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(@PathVariable String id) {
        try {
            groupService.deleteGroup(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new ScimError(404, "notFound", e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping("/{groupId}/members/{userId}")
    public ResponseEntity<ScimGroup> addMemberToGroup(
            @PathVariable String groupId,
            @PathVariable String userId) {
        return ResponseEntity.ok(groupService.addMemberToGroup(groupId, userId));
    }

    @DeleteMapping("/{groupId}/members/{userId}")
    public ResponseEntity<ScimGroup> removeMemberFromGroup(
            @PathVariable String groupId,
            @PathVariable String userId) {
        return ResponseEntity.ok(groupService.removeMemberFromGroup(groupId, userId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<ScimListResponse<ScimGroup>> getGroupsForUser(@PathVariable String userId) {
        return ResponseEntity.ok(groupService.getGroupsForUser(userId));
    }
}
