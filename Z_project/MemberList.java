import java.util.*;
import java.io.*;
public class MemberList implements Serializable {
    private static final long serialVersionUID = 1L;
    private List members = new LinkedList();
    private static MemberList memberList;

    private MemberList() {
    }
    public static MemberList instance() {
        if (memberList == null) {
            return (memberList = new MemberList());
        } else {
            return memberList;
        }
    }

    public boolean insertMember(Member member){
        members.add(member);
        return true;
    }

    public Iterator getMembers(){
        return members.iterator();
    }

    public Member findMember(String target){
        Iterator iterator = members.iterator();

        while (iterator.hasNext()) {
            Member member = (Member)iterator.next();
            if (member.getName().equalsIgnoreCase(target)) {
                return member;
            }
        }
        return null;
    }

    public Member search(String memberId) {
        for (Iterator iterator = members.iterator(); ; iterator.hasNext()) {
            Member member = (Member) iterator.next();
            if (member.getId().equals(memberId)) {
                return member;
            }
        }
        return null;
    }


    private void writeObject(java.io.ObjectOutputStream output) {
        try {
            output.defaultWriteObject();
            output.writeObject(memberList);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
    private void readObject(java.io.ObjectInputStream input) {
        try {
            if (memberList != null) {
                return;
            } else {
                input.defaultReadObject();
                if (memberList == null) {
                    memberList = (MemberList) input.readObject();
                } else {
                    input.readObject();
                }
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        } catch(ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
    }
    public String toString() {
        return members.toString();
    }






}
