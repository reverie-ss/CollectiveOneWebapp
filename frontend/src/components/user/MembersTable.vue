<template lang="html">
  <div class="members-table-container">
    <div v-if="canEdit" class="new-member-row">
      <div class="w3-row w3-container">
        <label class="">
          <b>add user:</b>
        </label>
      </div>

      <div class="w3-row user-selector-row">
        <div class="w3-col m7">
          <app-user-selector
            class="user-selector"
            anchor="c1Id" label="nickname" :resetTrigger="resetUserSelector"
            url="/1/users/suggestions"
            @select="userSelected($event)">
          </app-user-selector>
        </div>
        <div class="w3-col m5">
          <div class="w3-row-padding">
            <div class="w3-col s8 w3-center">
              <select id="T_memberRoleDropDown" class="role-select w3-select w3-round" v-model="newMember.role">
                <option value="" disabled>Choose role</option>
                <option value="ADMIN">ADMIN</option>
                <option value="EDITOR">EDITOR</option>
                <option value="MEMBER" selected>MEMBER</option>
              </select>
            </div>
            <div class="w3-col s4 w3-center">
              <button id="T_addButton" type="button" class="add-btn w3-button app-button" @click="add()">add</button>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="w3-row table-row">
      <table class="w3-table w3-striped w3-bordered w3-centered table-element">
        <thead>
          <tr>
            <th class="avatar-col">AVATAR</th>
            <th>NICKNAME</th>
            <th v-for="asset in assets">{{ asset.assetName }}</th>
            <th class="role-col">ROLE</th>
            <th v-if="isLoggedAnAdmin">DELETE</th>
            <th v-if="showLeaveCol">LEAVE</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="member in membersSorted">
            <td class="avatar-col">
              <app-user-avatar :user="member.user" :showName="false"></app-user-avatar>
            </td>
            <td id="T_username">{{ member.user.nickname }}</td>
            <td v-for="asset in assets">
              {{ ownedOfThisAsset(asset, member) }}
            </td>
            <td class="noselect w3-center role-col">
              <select v-if="canEdit" v-model="member.role" @change="$emit('role-updated', member)" class="role-select w3-select w3-round">
                <option value="" disabled>Choose role</option>
                <option value="ADMIN">ADMIN</option>
                <option value="EDITOR">EDITOR</option>
                <option value="MEMBER" selected>MEMBER</option>
              </select>
              <p v-else>
                <span class="role-tag w3-tag w3-round gray-1">
                  {{ member.role ? member.role : 'MEMBER' }}
                </span>
              </p>
            </td>
            <td v-if="isLoggedAnAdmin" id="T_deleteUser">
              <i 
                id="T_peopleDeleteIcon" 
                v-if="!removingThisMember(member)"
                @click="removeMember(member)"
                class="fa fa-times-circle-o w3-xlarge gray-1-color w3-button" aria-hidden="true">
              </i>
              <div v-else class="">
                <button id="T_cancelButton_DeletePople" @click="removeMemberCancelled()" class="w3-button app-button-light">Cancel Delete</button>
                <button id="T_confirmButton_DeletePople" @click="removeMemberConfirmed()" class="w3-button app-button-danger">Confirm Delete</button>
              </div>
            </td>
            <td v-if="showLeaveCol">
              <div v-if="isLoggedThis(member) && !isLoggedAnAdmin">
                <i v-if="!leaving"
                  @click="leave(member)"
                  class="fa fa-times-circle-o w3-xlarge gray-1-color w3-button" aria-hidden="true">
                </i>
                <div v-else class="">
                  <button @click="removeMemberCancelled()" class="w3-button app-button-light">Cancel</button>
                  <button @click="removeMemberConfirmed()" class="w3-button app-button-danger">Confirm</button>
                </div>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
import UserAvatar from '@/components/user/UserAvatar.vue'
import UserSelector from '@/components/user/UserSelector.vue'

export default {
  components: {
    'app-user-avatar': UserAvatar,
    'app-user-selector': UserSelector
  },

  props: {
    members: Array,
    canEdit: {
      type: Boolean,
      default: false
    },
    assets: {
      type: Array,
      default: () => {
        return []
      }
    }
  },

  data () {
    return {
      newMember: {
        user: null,
        role: 'MEMBER'
      },
      resetUserSelector: false,
      memberToBeRemoved: null
    }
  },

  computed: {
    membersSorted () {
      if (this.assets.length > 0) {
        var membersTemp = JSON.parse(JSON.stringify(this.members))
        return membersTemp.sort((a, b) => { return b.receivedAssets[0].ownedByThisHolder - a.receivedAssets[0].ownedByThisHolder })
      } else {
        return this.members
      }
    },
    isLoggedAnAdmin () {
      return this.$store.getters.isLoggedAnAdmin
    },
    isLoggedHere () {
      if (!this.$store.state.user.profile) {
        return false
      }

      for (var ix in this.members) {
        if (this.members[ix].user.c1Id === this.$store.state.user.profile.c1Id) {
          return true
        }
      }
      return false
    },
    leaving () {
      if (!this.memberToBeRemoved || !this.$store.state.user.profile) {
        return false
      }
      return this.memberToBeRemoved.user.c1Id === this.$store.state.user.profile.c1Id
    },
    showLeaveCol () {
      return this.isLoggedHere && !this.isLoggedAnAdmin
    }
  },

  methods: {
    removeMember (member) {
      setTimeout(() => {
        this.memberToBeRemoved = member
      }, 500)
    },
    leave (member) {
      setTimeout(() => {
        this.memberToBeRemoved = member
      }, 500)
    },
    removingThisMember (member) {
      if (this.memberToBeRemoved) {
        return this.memberToBeRemoved.user.c1Id === member.user.c1Id
      }
      return false
    },
    leaveThisConfirmed () {
      this.$emit('remove', JSON.parse(JSON.stringify(this.memberToBeRemoved)))
      this.memberToBeRemoved = null
    },
    isLoggedThis (member) {
      if (!this.$store.state.user.profile) {
        return false
      }
      return member.user.c1Id === this.$store.state.user.profile.c1Id
    },
    removeMemberCancelled () {
      this.memberToBeRemoved = null
    },
    removeMemberConfirmed () {
      this.$emit('remove', JSON.parse(JSON.stringify(this.memberToBeRemoved)))
      this.memberToBeRemoved = null
    },
    add () {
      this.resetUserSelector = !this.resetUserSelector
      this.$emit('add', this.newMember)
    },
    userSelected (user) {
      this.newMember.user = user
    },
    ownedOfThisAsset (asset, member) {
      for (var ix in member.receivedAssets) {
        if (member.receivedAssets[ix].assetId === asset.assetId) {
          return member.receivedAssets[ix].ownedByThisHolder > 0 ? member.receivedAssets[ix].ownedByThisHolder.toFixed(2) : '-'
        }
      }
      return '-'
    }
  }
}
</script>

<style scoped>

.members-table-container {
  max-height: 80vh;
  overflow-y: auto;
}

.table-row {
  margin: 0 auto;
  margin-bottom: 25px;
}

.avatar-col {
  /*width: 50px;*/
}

.role-col {
  /*width: 120px;*/
}

.role-select {
  max-width: 120px;
}

.user-selector-row .w3-col {
  margin-bottom: 10px;
}

</style>
