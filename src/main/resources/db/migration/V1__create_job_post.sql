CREATE TABLE public.job_post (
    post_id int4 NOT NULL,
    post_profile VARCHAR(255),                     
    post_desc VARCHAR(255),                        
    req_experience INT,
    post_tech_stack varchar NULL,                       
    
    version BIGINT DEFAULT 0,                      
    created_by VARCHAR(64),                        
    created_at TIMESTAMP,                      
    updated_by VARCHAR(64),
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP ,
     -- constraints
    CONSTRAINT pk_post_id PRIMARY KEY (post_id)
);