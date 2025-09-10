package cn.handsome.scim.controller;

import cn.handsome.scim.model.ScimError;
import cn.handsome.scim.model.ScimPatch;
import cn.handsome.scim.model.ScimUser;
import cn.handsome.scim.service.ScimUserService;
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
@RequestMapping("/scim/v2/Users")
@Api(value = "user", tags = "User 服务")
public class ScimUserController extends BaseController {

    private final ScimUserService userService;

    @Autowired
    public ScimUserController(ScimUserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody ScimUser user) {
        try {
            ScimUser createdUser = userService.createUser(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new ScimError(400, "invalidValue", e.getMessage()),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable String id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new ScimError(404, "notFound", e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @GetMapping
    public ResponseEntity<?> getUsers(
            @RequestParam(defaultValue = "1") int startIndex,
            @RequestParam(defaultValue = "100") int count,
            @RequestParam(required = false) String filter,
            @RequestParam(required = false) String attributes
    ) {
        return ResponseEntity.ok(userService.getUsers(filter, attributes, startIndex, count));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable String id, @RequestBody ScimUser user) {
        try {
            return ResponseEntity.ok(userService.updateUser(id, user));
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new ScimError(404, "notFound", e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ScimUser> patchUser(@PathVariable String id, @RequestBody ScimPatch patch) {
        return ResponseEntity.ok(userService.patchUser(id, patch));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                    new ScimError(404, "notFound", e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

    @PostMapping("/{userId}/groups/{groupId}")
    public ResponseEntity<ScimUser> addUserToGroup(
            @PathVariable String userId,
            @PathVariable String groupId) {
        return ResponseEntity.ok(userService.addUserToGroup(userId, groupId));
    }

    @DeleteMapping("/{userId}/groups/{groupId}")
    public ResponseEntity<ScimUser> removeUserFromGroup(
            @PathVariable String userId,
            @PathVariable String groupId) {
        return ResponseEntity.ok(userService.removeUserFromGroup(userId, groupId));
    }
}
