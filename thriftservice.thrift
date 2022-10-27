namespace java thriftserviceprofile

typedef i32 ProfileID

struct Profile {
    1: ProfileID id,
    2: string name,
    3: string email,
    4: string address,
    5: i32 phone
}

exception InvalidOperation {
  1: i32 what,
  2: string why
}

service ProfileService {
    
    Profile getProfileById(1: ProfileID id) throws (1: InvalidOperation ouch),

    list<Profile> getMultiProfiles(1: list<ProfileID> ids) throws (1: InvalidOperation ouch),

    void addProfile(1: Profile profile) throws (1: InvalidOperation ouch),

    void deleteProfile(1: ProfileID id) throws (1: InvalidOperation ouch),
}