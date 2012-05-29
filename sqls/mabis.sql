create database mabis DEFAULT CHARACTER SET utf8 COLLATE utf8_polish_ci;
use mabis;

/**
 * creating covers table to hold all covers images for every item in the database (movie,book,audio)
 */
create table if not exists mabis.picture (
    idPicture int(11) auto_increment,
    object longblob not null,
    primary key (idPicture)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE = utf8_polish_ci comment='images';

/**
 * creating users table
 */
create table if not exists mabis.user (
    idUser int(11) auto_increment,
    object longblob not null,
    avatarFK int(11) null default '0',
    primary key (idUser),
    key avatarUserKey (avatarFK) using btree,
    constraint avatarUserRef foreign key (avatarFK)
        references picture (idPicture)
        on delete cascade
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE = utf8_polish_ci comment='table available in offline and online database';
/**
 * genre table, can be used by audio, movies and cds at the same time
 */
create table if not exists mabis.genre (
    idGenre int(11) auto_increment,
    object longblob not null,
    type varchar(15) not null,
    primary key (idGenre)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE = utf8_polish_ci;

/**
 * authors for items, mainly for movies and books, thinking how to use it for audios
 */
create table if not exists mabis.author (
    idAuthor int(11) auto_increment,
    object longblob not null,
    avatarFK int(11) null default '0',
    type varchar(15) not null,
    primary key (idAuthor),
    key avatarAuthorKey (avatarFK) using btree,
    constraint avatarAuthorRef foreign key (avatarFK)
        references picture (idPicture)
        on delete set null
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE = utf8_polish_ci;

/**
 * creating movie table + required additional tables
 */
create table if not exists mabis.movie (
    idMovie int(11) auto_increment,
    object longblob not null,
    title varchar(100) not null,
    directorFK int(11) null default '0' comment 'references to idAuthor',
    coverFK    int(11) null default '0' comment 'references to idPicture',
    genreFK    int(11) null default '0' comment 'references to idGenre',
    primary key (idMovie),
    key movieCoverRef (coverFK) using btree,
    key movieDirectorRef (directorFK) using btree,
    key movieGenreRef (genreFK) using btree,
    constraint movieCoverRef foreign key (coverFK)
        references picture (idPicture)
        on delete set null,
    constraint movieDirectorRef foreign key (directorFK)
        references author (idAuthor)
        on delete set null,
    constraint movieGenreRef foreign key (genreFK)
        references genre (idGenre)
        on delete set null
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE = utf8_polish_ci comment='every movie located in database';

/**
 * because of the fact that single movie can be presented in more than one user's collection 
 * MM table is created
 */
create table if not exists mabis.movieUser (
    idMovieUser int(11) auto_increment,
    idUser int(11) not null,
    idMovie int(11) not null,
    primary key (idMovieUser),
    key movieUserUserRef (idUser) using btree,
    key movieUsermovieRef (idMovie) using btree,
    constraint movieUserUserRef foreign key (idUser)
        references user (idUser)
        on delete cascade,
    constraint movieUserMovieRef foreign key (idMovie)
        references movie (idMovie)
        on delete cascade
)  ENGINE=InnoDB;

/**
 * creating books table + required additional tables
 */
create table if not exists mabis.book (
    idBook int(11) auto_increment,
    object longblob not null,
    title varchar(100) not null,
    writerFK int(11) null default '0' comment 'references to idAuthor',
    coverFK    int(11) null default '0' comment 'references to idPicture',
    genreFK    int(11) null default '0' comment 'references to idGenre',
    primary key (idBook),
    key bookGenreRef (genreFK) using btree,
    key bookCoverRef (coverFK) using btree,
    key bookWriterRef (writerFK) using btree,
    constraint bookWriterRef foreign key (writerFK)
        references author (idAuthor)
        on delete set null,
    constraint bookGenreRef foreign key (genreFK)
        references genre (idGenre)
        on delete set null,
    constraint bookCoverRef foreign key (coverFK)
        references picture (idPicture)
        on delete set null
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE = utf8_polish_ci;

/**
 * creating multi-multi joining table for books and users
 */
create table if not exists mabis.bookUser (
    idBookUser int(11) auto_increment,
    idUser int(11) not null,
    idBook int(11) not null,
    primary key (idBookUser),
    key bookUserUserRef (idUser) using btree,
    key bookUserBookRef (idBook) using btree,
    constraint bookUserUserRef foreign key (idUser)
        references user (idUser)
        on delete cascade,
    constraint bookUserBookRef foreign key (idBook)
        references book (idBook)
        on delete cascade
)  ENGINE=InnoDB;

/**
 * creating audios table + required additional tables
 */
create table if not exists mabis.audioAlbum (
    idAudio int(11) auto_increment,
    object longblob not null,
    title varchar(100) not null,
    bandFK int(11) null default '0' comment 'references to idAuthor',
    coverFK    int(11) null default '0' comment 'references to idPicture',
    genreFK    int(11) null default '0' comment 'references to idGenre',
    primary key (idAudio),
    key audioCoverRef (coverFK) using btree,
    key audioBandRef (artistFK) using btree,
    key audioGenreRed (genreFK) using btree,
    constraint audioBandRef foreign key (bandFK)
        references author (idAuthor)
        on delete set null on update restrict,
    constraint audioCoverRef foreign key (coverFK)
        references picture (idPicture)
        on delete set null on update cascade,
    constraint audioGenreRef foreign key (genreFK)
        references genre (idGenre)
        on delete set null
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE = utf8_polish_ci;

/**
 * creating audio - user multi table, as one audio can be in more than one user collection
 */
create table if not exists mabis.audioUser (
    idAudioUser int(11) auto_increment,
    idUser int(11) not null,
    idAudio int(11) not null,
    primary key (idAudioUser),
    key audioUserUserRef (idUser) using btree,
    key audioUserAudioRef (idAudio) using btree,
    constraint audioUserUserRef foreign key (idUser)
        references user (idUser)
        on delete cascade,
    constraint audioUserAudioRef foreign key (idAudio)
        references audioAlbum (idAudio)
        on delete cascade
)  ENGINE=InnoDB;
