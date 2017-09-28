package ru.glaizier.todo.persistence.memory.user;

//@Repository
//@Profile("memory")
public class UserDaoImpl implements OverrideUserDao {
//
//    private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
//
//    private final MemoryDb db;
//
//    @Autowired
//    public UserDaoImpl(MemoryDb db) {
//        this.db = db;
//    }
//
//    @Override
//    public User findUserByLogin(String login) {
//        log.info("findUserByLogin memory implementation...");
//        return db.getUser(login);
//    }
//
//    @Override
//    public User findUserByLoginAndPassword(String login, char[] password) {
//        log.info("findUserByLoginAndPassword memory implementation...");
//        User user = findUserByLogin(login);
//        return user == null ?
//                null
//                :
//                Arrays.equals(user.getPassword(), password) ? user : null;
//    }
//
//    @Override
//    public <S extends User> S save(S s) {
//        log.info("save memory implementation...");
//        db.addUser(s);
//        return s;
//    }
//
//    @Override
//    public void delete(String login) {
//        log.info("delete memory implementation...");
//        db.removeUser(login);
//    }
}
