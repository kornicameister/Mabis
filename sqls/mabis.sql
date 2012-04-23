create database mabis DEFAULT CHARACTER SET utf8 COLLATE utf8_polish_ci;
use mabis;

/**
 * creating covers table to hold all covers images for every item in the database (movie,book,audio)
 */
create table if not exists mabis.picture (
    idPicture int(11) auto_increment,
    image varchar(150) NOT NULL,
    hash varchar(40) not null comment 'field to check validity of image transferr',
    primary key (idPicture)
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE = utf8_polish_ci comment='images';

/**
 * creating users table
 */
create table if not exists mabis.user (
    idUser int(11) auto_increment,
    login varchar(15) not null unique comment 'user login',
    email varchar(40) null default 'mail@mail.com' comment 'email of the user',
    password char(36) not null comment 'user password, hashed with md5sum',
    firstName varchar(30) not null comment 'first name',
    lastName varchar(40) not null comment 'last name',
    avatar int(11) null default '0',
    primary key (idUser),
    unique key userLoginKey (login) using btree,
    key avatarUserKey (avatar) using btree,
    constraint avatarUserRef foreign key (avatar)
        references picture (idPicture)
        on delete set null
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE = utf8_polish_ci comment='table available in offline and online database';
/**
 * genre table, can be used by audio, movies and cds at the same time
 */
create table if not exists mabis.genre (
    idGenre int(11) auto_increment,
    genre varchar(30) null default 'empty',
    primary key (idGenre),
    index genreIndex (genre) using btree
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE = utf8_polish_ci;

/**
 * authors for items, mainly for movies and books, thinking how to use it for audios
 */
create table if not exists mabis.author (
    idAuthor int(11) auto_increment,
    firstName varchar(30) not null,
    lastName varchar(40) not null,
    avatar int(11) null default '0',
    primary key (idAuthor),
    index authorLastNameIndex (lastName),
    key avatarAuthorKey (avatar) using btree,
    constraint avatarAuthorRef foreign key (avatar)
        references picture (idPicture)
        on delete set null
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE = utf8_polish_ci;

/**
 * table of metal bands ;-) for audios
 */
create table if not exists mabis.band (
    idBand int(11) auto_increment,
    name varchar(30) not null,
    description text not null,
    url varchar(50) null default 'empty',
    avatar int(11) null default '0',
    masterGenre int(11) null default '0' comment 'references to idGenre',
    tagCloud tinytext not null,
    primary key (idBand),
    index bandIndex (name),
    key bandGenreRef (masterGenre) using btree,
    constraint bandGenreRef foreign key (masterGenre)
        references genre (idGenre)
        on delete set null,
    key avatarBandKey (avatar) using btree,
    constraint avatarBandRef foreign key (avatar)
        references picture (idPicture)
        on delete set null
)  ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE = utf8_polish_ci;

/**
 * creating movie table + required additional tables
 */
create table if not exists mabis.movie (
    idMovie int(11) auto_increment,
    titleOriginal varchar(50) not null comment 'movie title in original language',
    titleLocale varchar(50) null default 'empty' comment 'translated title of the movie',
    duration time not null comment 'lenght of the movie',
    description mediumtext not null comment 'movie description',
    cover int(11) null default '0',
    director int(11) null default '0' comment 'references to idAuthor',
    genre int(11) null default '0',
    primary key (idMovie),
    key movieCoverRef (cover) using btree,
    key movieDirectorRef (director) using btree,
    key movieGenreRef (genre) using btree,
    constraint movieCoverRef foreign key (cover)
        references picture (idPicture)
        on delete set null,
    constraint movieDirectorRef foreign key (director)
        references author (idAuthor)
        on delete set null,
    constraint movieGenreRef foreign key (genre)
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
    isbn varchar(13) not null unique key comment 'isbn number',
    titleOriginal varchar(50) not null comment 'title in original language',
    titleLocale varchar(50) null default 'empty' comment 'translated title of the movie',
    genre int(11) null default '0',
    pages int(11) unsigned not null,
    cover int(11) null default '0',
    writer int(11) null default '0',
    primary key (idBook),
    key bookGenreRef (genre) using btree,
    key bookCoverRef (cover) using btree,
    key bookWriterRef (writer) using btree,
    index isbnIndex (isbn) using btree,
    constraint bookWriterRef foreign key (writer)
        references author (idAuthor)
        on delete set null,
    constraint bookGenreRef foreign key (genre)
        references genre (idGenre)
        on delete set null,
    constraint bookCoverRef foreign key (cover)
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
    title varchar(50) not null,
    frontCover int(11) null default '0',
    backCover int(11) null default '0',
    cdCover int(11) null default '0',
    tagCloud tinytext not null,
    trackList tinytext not null,
    artist int(11) null default '0',
    totalTime smallint(11) not null,
    primary key (idAudio),
    key audioFrontCoverRef (frontCover) using btree,
    key audioBackCoverRef (backCover) using btree,
    key audioBandRef (artist) using btree,
    key audioCdCover (cdCover) using btree,
    constraint audioBandRef foreign key (artist)
        references band (idBand)
        on delete set null on update restrict,
    constraint audioFrontCoverRef foreign key (frontCover)
        references picture (idPicture)
        on delete set null on update cascade,
    constraint audioBackCoverRef foreign key (backCover)
        references picture (idPicture)
        on delete set null on update cascade,
    constraint audioCdCover foreign key (cdCover)
        references picture (idPicture)
        on delete set null on update cascade
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

-- views 
-- 1. select user full information
create algorithm=temptable definer=root@localhost sql security definer view UserListView as select * from user join picture on user.avatar = picture.idpicture order by user.login desc;
-- 2. select band full information, all what'is in band + genres
create algorithm=temptable definer=root@localhost sql security definer view BandListView as select * from band join genre on band.masterGenre = genre.idGenre order by band.masterGenre desc;
-- 3. select full book information, all what'is in book + information about author and genre
create algorithm=temptable definer=root@localhost sql security definer view BookListView as SELECT  
        b.idBook,
        b.isbn,
        b.titleOriginal,
        b.titleLocale,
        b.pages,
        g.genre,
        p.image,
        a.idAuthor,
        a.firstName,
        a.lastName,
        a.avatar
        FROM mabis.book as b    inner join mabis.genre as g on b.genre=g.idGenre 
                                inner join mabis.picture p on b.cover=p.idPicture 
                                inner join mabis.author a on b.writer = a.idAUthor
        group by b.isbn order by a.idAuthor asc;
-- 4. select full movie information
create algorithm=temptable definer=root@localhost sql security definer view MovieListView as SELECT  
        m.idMovie,
        m.titleOriginal,
        m.titleLocale,
        m.duration,
        m.genre as idGenre,
        g.genre as genre,
        m.cover as coverId,
        p.image as coverImage,
        p.hash as coverHash,
        a.idAuthor,
        a.firstName as authorFirstName,
        a.lastName as authorLastName,
        (select p.idPicture from mabis.picture p where p.idPicture = a.avatar) as authorImageID,
        (select p.image from mabis.picture p where p.idPicture = a.avatar) as authorImageFile,
        (select p.hash from mabis.picture p where p.idPicture = a.avatar) as authorImageHash
        FROM mabis.movie as m    inner join mabis.genre as g on m.genre=g.idGenre 
                                inner join mabis.picture p on m.cover=p.idPicture 
                                inner join mabis.author a on m.director = a.idAUthor
        group by m.titleOriginal order by a.idAuthor asc;
-- 5. select full audio album information
create algorithm=temptable definer=root@localhost sql security definer view AudioAlbumListView as SELECT  
        aa.idAudio,
        aa.title,
        aa.tagCloud,
        aa.trackList,
        aa.totalTime,
        (select p.image from mabis.picture p where p.idPicture = aa.frontCover) as frontCover,
        (select p.image from mabis.picture p where p.idPicture = aa.backCover) as backCover,
        (select p.image from mabis.picture p where p.idPicture = aa.cdCover) as cdCover,
        b.idBand,
        b.name,
        (select p.image from mabis.picture p where p.idPicture = b.avatar) as bandImage,
        (select p.hash from mabis.picture p where p.idPicture = b.avatar) as bandImageHash
        FROM mabis.audioAlbum as aa inner join mabis.band b on aa.artist = b.idBand
        group by aa.title order by b.idBand asc
